package com.clt.service.impl;

import com.clt.dto.ProblemCreationOrUpdateDTO;
import com.clt.enums.ProblemDifficulty;
import com.clt.exception.*;
import com.clt.vo.ProblemRecommendVO;
import com.clt.vo.ProblemTitleInfoVO;
import com.clt.enums.SubmissionStatus;
import com.clt.mapper.ProblemMapper;
import com.clt.entity.*;
import com.clt.service.ProblemSampleService;
import com.clt.service.ProblemService;
import com.clt.service.SubmissionService;
import com.clt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemSampleService problemSampleService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private TagService tagService;

    /**
     * 获取题目列表,用于表格展示
     */
    @Override
    public Integer getProblemListCount() {
        return problemMapper.getProblemListCount();
    }

    /**
     * 根据题目ID获取题目详情
     */
    @Override
    public Problem getProblemById(Integer id) throws NoProblemException {
        Problem problem = problemMapper.getProblemById(id);
        if (problem == null) {
            throw new NoProblemException("题目不存在");
        }
        problem.setTags(tagService.getProblemTagListByProblemId(id));
        problem.setSamples(problemSampleService.getProblemSampleListByProblemIdIfVisible(id));
        return problem;
    }

    /**
     * 添加问题
     */
    @Transactional
    @Override
    public Problem CreateProblem(ProblemCreationOrUpdateDTO dto) throws RuntimeException {

        if (problemMapper.getIdByTitleAndDescription(dto.getTitle(), dto.getDescription()) != null) {
            throw new ProblemIsExistException("题目已存在");
        }

        if (dto.getTitle() == null || dto.getTitle().isEmpty() || dto.getDescription() == null || dto.getDescription().isEmpty()) {
            throw new NullProblemTitleAndDescriptionException("标题或描述不能为空");
        }

        if (dto.getInputFormat() == null || dto.getInputFormat().isEmpty()) {
            throw new NullProblemInputFormatException("输入格式不能为空");
        }

        if (dto.getOutputFormat() == null || dto.getOutputFormat().isEmpty()) {
            throw new NullProblemOutputFormatException("输出格式不能为空");
        }

        if (dto.getSamples() == null || dto.getSamples().isEmpty()) {
            throw new NullProblemSampleException("题目样例不能为空");
        }

        Problem problem = new Problem();
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setInputFormat(dto.getInputFormat());
        problem.setOutputFormat(dto.getOutputFormat());
        problem.setTimeLimit(dto.getTimeLimit());
        problem.setMemoryLimit(dto.getMemoryLimit());
        problem.setDifficulty(dto.getDifficulty());
        problem.setHint(dto.getHint());
        int rows = problemMapper.insert(problem);
        if (rows == 0) {
            throw new RuntimeException("添加题目失败");
        }
        Integer id = problem.getId();

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            AtomicBoolean flag = new AtomicBoolean(true);
            dto.getTags().forEach(t -> {
                Tag tag = new Tag();
                tag.setProblemId(id);
                tag.setName(t.getName());
                tag.setColor(t.getColor());
                if (!tagService.isExist(tag.getName())){
                    flag.set(tagService.insert(tag));
                }else {
                    tag.setId(tagService.getIdByName(tag.getName()));
                }
                flag.set(tagService.insertRelationship(tag));
            });
            if (!flag.get()) {
                throw new RuntimeException("添加标签失败");
            }
        }

        if (dto.getSamples() != null) {
            AtomicBoolean flag = new AtomicBoolean(true);
            dto.getSamples().forEach(s -> {
                ProblemSample sample = new ProblemSample();
                sample.setProblemId(id);
                sample.setSampleOrder(s.getSampleOrder());
                sample.setInputContent(s.getInputContent());
                sample.setOutputContent(s.getOutputContent());
                sample.setIsExample(s.getIsExample());
                flag.set(problemSampleService.insert(sample));
            });
            if (!flag.get()) {
                throw new RuntimeException("添加样例失败");
            }
        }

        problem.setSamples(problemSampleService.getProblemSampleListByProblemIdIfVisible(id));

        return problem;
    }

    /**
     * 更新问题
     */
    @Transactional
    @Override
    public Problem UpdateProblem(ProblemCreationOrUpdateDTO dto) throws RuntimeException {

        if (dto.getId() == null) {
            throw new NullProblemIdException("题目ID不能为空");
        }

        if (dto.getInputFormat() == null || dto.getInputFormat().isEmpty()) {
            throw new NullProblemInputFormatException("输入格式不能为空");
        }

        if (dto.getOutputFormat() == null || dto.getOutputFormat().isEmpty()) {
            throw new NullProblemOutputFormatException("输出格式不能为空");
        }

        if (dto.getSamples() == null || dto.getSamples().isEmpty()) {
            throw new NullProblemSampleException("题目样例不能为空");
        }

        Problem problem = problemMapper.getProblemById(dto.getId());
        if (problem == null) {
            throw new ProblemIsNotExistException("题目不存在");
        }

        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setInputFormat(dto.getInputFormat());
        problem.setOutputFormat(dto.getOutputFormat());
        problem.setTimeLimit(dto.getTimeLimit());
        problem.setMemoryLimit(dto.getMemoryLimit());
        problem.setDifficulty(dto.getDifficulty());
        problem.setHint(dto.getHint());
        if (problemMapper.update(problem) == 0) {
            throw  new RuntimeException("更新题目失败");
        }

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            tagService.deleteByProblemId(dto.getId());
            AtomicBoolean flag = new AtomicBoolean(true);
            dto.getTags().forEach(t -> {
                Tag tag = new Tag();
                tag.setProblemId(problemMapper.getIdByTitleAndDescription(problem.getTitle(), problem.getDescription()));
                tag.setName(t.getName());
                tag.setColor(t.getColor());
                if (!tagService.isExist(tag.getName())){
                    flag.set(tagService.insert(tag));
                }else {
                    tag.setId(tagService.getIdByName(tag.getName()));
                    flag.set(tagService.setTagColor(tag));
                }
                flag.set(tagService.insertRelationship(tag));
            });
            if (!flag.get()) {
                throw new RuntimeException("更新标签失败");
            }
        }

        if (dto.getSamples() != null) {
            problemSampleService.deleteByProblemId(dto.getId());
            AtomicBoolean flag = new AtomicBoolean(true);
            dto.getSamples().forEach(t -> {
                ProblemSample sample = new ProblemSample();
                sample.setProblemId(problemMapper.getIdByTitleAndDescription(problem.getTitle(), problem.getDescription()));
                sample.setSampleOrder(t.getSampleOrder());
                sample.setInputContent(t.getInputContent());
                sample.setOutputContent(t.getOutputContent());
                sample.setIsExample(t.getIsExample());
                flag.set(problemSampleService.insert(sample));
            });
            if (!flag.get()) {
                throw new RuntimeException("更新样例失败");
            }
        }

        return problem;
    }

    /**
     * 删除题目
     */
    @Transactional
    @Override
    public boolean deleteProblem(Integer id) throws RuntimeException {

        if (id == null) {
            throw new NullProblemIdException("题目ID不能为空");
        }
        if (problemMapper.getProblemById(id) == null) {
            throw new ProblemIsNotExistException("题目不存在");
        }

        int ProblemRows;
        try {
            tagService.deleteByProblemId(id);
            problemSampleService.deleteByProblemId(id);
            List<Integer> submissionIds = submissionService.getIdByProblemId(id);
            submissionIds.forEach(s -> submissionService.deleteTestCasesBySubmissionId(s));
            submissionService.deleteByProblemId(id);
            List<Integer> solutionIds = problemMapper.getSolutionIdByProblemId(id);
            solutionIds.forEach(s -> {
                problemMapper.deleteSolutionContentBySolutionId(s);
                problemMapper.deleteSolutionCommentBySolutionId(s);
            });
            problemMapper.deleteSolutionByProblemId(id);
            problemMapper.deleteUserProblemStatusByProblemId(id);
            problemMapper.deleteSolvedProblemCountByProblemId(id);
            ProblemRows = problemMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ProblemRows > 0;
    }

    /**
     * 分页查询
     */
    @Override
    public List<ProblemTitleInfoVO> getProblemListOfPage(Integer start, Integer pageSize) {
        List<ProblemTitleInfoVO> problemTitleInfoVOList = new ArrayList<>();
        List<Problem> problemListOfPage = problemMapper.getProblemTilesListOfPage(start, pageSize);
        problemListOfPage.forEach(p -> {
            int total = 0;
            int count = 0;
            double passRate;
            List<String> status = submissionService.getStatusByProblemId(p.getId());
            List<Tag> tags = tagService.getProblemTagListByProblemId(p.getId());
            if (!status.isEmpty()) {
                total += status.size();
                for (String s : status) {
                    if (s.equals(SubmissionStatus.ACCEPTED.message)) count++;
                }
                passRate = count * 1.0 / total;
            }else {
                passRate = 0.0;
            }
            String difficulty = ProblemDifficulty.getMassage(p.getDifficulty());
            String passRateStr = String.format("%.2f", passRate * 100) + "%";
            problemTitleInfoVOList.add(new ProblemTitleInfoVO(p.getId(), p.getTitle(), difficulty, passRateStr, tags));
        });
        return problemTitleInfoVOList;
    }

    /**
     * 获取推荐题目
     */
    @Override
    public List<ProblemRecommendVO> getRecommendProblems() {
        List<ProblemRecommendVO> problemRecommendVOList = new ArrayList<>();
        List<Problem> problemList = problemMapper.getRecommendProblem();
        problemList.forEach(p -> {
            int total = 0;
            int count = 0;
            double passRate;
            List<String> status = submissionService.getStatusByProblemId(p.getId());
            if (!status.isEmpty()) {
                total += status.size();
                for (String s : status) {
                    if (s.equals(SubmissionStatus.ACCEPTED.message)) count++;
                }
                passRate = count * 1.0 / total;
            }else {
                passRate = 0.0;
            }
            String difficulty = ProblemDifficulty.getMassage(p.getDifficulty());
            String passRateStr = String.format("%.2f", passRate * 100) + "%";
            problemRecommendVOList.add(new ProblemRecommendVO(p.getId(), p.getTitle(), p.getDescription(), difficulty, passRateStr));
        });
        problemRecommendVOList.sort(Comparator.comparing(ProblemRecommendVO::getPassRate).reversed());
        return problemRecommendVOList.subList(0, 6);
    }

    /**
     * 搜索题目
     */
    @Override
    public List<ProblemTitleInfoVO> searchProblem(String keyword) {
        List<ProblemTitleInfoVO> problemTitleInfoVOList = new ArrayList<>();
        List<Problem> searchedProblemList = problemMapper.searchProblemList(keyword);
        searchedProblemList.forEach(p -> {
            int total = 0;
            int count = 0;
            double passRate;
            List<String> status = submissionService.getStatusByProblemId(p.getId());
            List<Tag> tags = tagService.getProblemTagListByProblemId(p.getId());
            if (!status.isEmpty()) {
                total += status.size();
                for (String s : status) {
                    if (s.equals(SubmissionStatus.ACCEPTED.message)) count++;
                }
                passRate = count * 1.0 / total;
            } else {
                passRate = 0.0;
            }

            problemTitleInfoVOList.add(new ProblemTitleInfoVO(p.getId(), p.getTitle(), ProblemDifficulty.getMassage(p.getDifficulty()), String.format("%.2f", passRate * 100) + "%", tags));
        });

        return problemTitleInfoVOList;
    }

    /**
     * 筛选题目
     */
    @Override
    public List<ProblemTitleInfoVO> filterProblem(String difficulty, Integer start, Integer pageSize) {
        if (difficulty != null && !difficulty.isEmpty()) {
            List<ProblemTitleInfoVO> problemTitleInfoVOList = new ArrayList<>();
            List<Problem> filteredProblemList = problemMapper.getProblemTilesListByDifficultyOfPage(ProblemDifficulty.getCode(difficulty), start, pageSize);
            filteredProblemList.forEach(p -> {
                int total = 0;
                int count = 0;
                double passRate;
                List<String> status = submissionService.getStatusByProblemId(p.getId());
                List<Tag> tags = tagService.getProblemTagListByProblemId(p.getId());
                if (!status.isEmpty()) {
                    total += status.size();
                    for (String s : status) {
                        if (s.equals(SubmissionStatus.ACCEPTED.message)) count++;
                    }
                    passRate = count * 1.0 / total;
                }else {
                    passRate = 0.0;
                }
                problemTitleInfoVOList.add(new ProblemTitleInfoVO(p.getId(), p.getTitle(), ProblemDifficulty.getMassage(p.getDifficulty()), String.format("%.2f", passRate * 100) + "%", tags));
            });
            return problemTitleInfoVOList;
        }else {
            return getProblemListOfPage(0, 10);
        }
    }
}
