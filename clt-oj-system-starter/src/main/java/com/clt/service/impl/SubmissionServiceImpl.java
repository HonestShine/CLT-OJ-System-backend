package com.clt.service.impl;

import com.clt.dto.SubmissionCodeDTO;
import com.clt.entity.JudgeResult;
import com.clt.entity.SolvedProblemCount;
import com.clt.entity.TestCase;
import com.clt.enums.SubmissionStatus;
import com.clt.exception.NullProblemIdException;
import com.clt.mapper.SolvedProblemCountMapper;
import com.clt.mapper.SubmissionMapper;
import com.clt.service.JudgeService;
import com.clt.service.SubmissionService;
import com.clt.vo.SubmissionResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private JudgeService judgeService;

    @Autowired
    private SolvedProblemCountMapper solvedProblemCountMapper;

    /**
     * 通过题目ID获取提交状态列表
     */
    @Override
    public List<String> getStatusByProblemId(Integer id) {
        return submissionMapper.getStatusByProblemId(id);
    }

    /**
     * 提交代码
     */
    @Transactional
    @Override
    public SubmissionResultVO submitCode(SubmissionCodeDTO submissionCodeDTO, Integer userId) throws RuntimeException {

        if (submissionCodeDTO.getProblemId() == null) {
            throw new NullProblemIdException("题目ID不能为空");
        }

        SubmissionResultVO result = new SubmissionResultVO();

        // 1. 调用判题机
        JudgeResult judgeResult = judgeService.judge(submissionCodeDTO);
        judgeResult.setUserId(userId);

        // 2. 保存提交结果到数据库
        submissionMapper.insertSubmission(judgeResult);
        Integer submissionId = judgeResult.getId(); // 获取生成的主键ID

        // 保存测试用例详情
        if (judgeResult.getTestResults() != null) {
            judgeResult.getTestResults().forEach(testCase -> {
                testCase.setSubmissionId(submissionId);
                submissionMapper.insertTestCase(testCase);
            });
        }

        // 3. 处理解题状态 (SolvedProblemCount)
        Integer problemId = submissionCodeDTO.getProblemId();
        boolean isAccepted = SubmissionStatus.ACCEPTED.message.equals(judgeResult.getStatus());

        // 查询用户针对该题目的已有记录
        SolvedProblemCount existingRecord = solvedProblemCountMapper.getIdByUserIdAndProblemId(userId, problemId);

        if (existingRecord == null) {
            // --- 情况A：用户第一次提交该题目 ---
            SolvedProblemCount newRecord = new SolvedProblemCount();
            newRecord.setUserId(userId);
            newRecord.setProblemId(problemId);
            // 第一次提交如果是AC，则标记为1，否则为0
            newRecord.setSolvedCount(isAccepted);
            solvedProblemCountMapper.insert(newRecord);

        } else {
            // --- 情况B：用户重复提交该题目 ---
            // 只有当 "之前没通过" 且 "这次通过了"，才更新状态
            // 注意：这里使用的是查询出来的 existingRecord，而不是 null 对象
            if (isAccepted && existingRecord.getSolvedCount() == false) {
                solvedProblemCountMapper.updateSolvedCount(userId, problemId);
            }
            // 如果之前已经AC了，或者这次依然没AC，都不需要更新数据库
        }

        // 4. 设置返回结果
        result.setId(submissionId);
        result.setProblemId(judgeResult.getProblemId());
        result.setUserId(userId);
        result.setCode(judgeResult.getCode());
        result.setLanguage(judgeResult.getLanguage());
        result.setStatus(judgeResult.getStatus());
        result.setSubmitTime(judgeResult.getSubmitTime());

        return result;
    }

    /**
     * 获取提交结果详情
     */
    @Override
    public JudgeResult getSubmissionResult(Integer id) {
        //查找submission表
        JudgeResult judgeResult = submissionMapper.getSubmissionInfoById(id);
        if (judgeResult == null) {
            return null;
        }
        //查询submission_test_cases表
        List<TestCase> testCases = submissionMapper.getSubmissionTestCasesBYSubmissionId(id);
        if (testCases == null) {
            return null;
        }
        judgeResult.setTestResults(testCases);
        return judgeResult;
    }

    /**
     * 通过题目ID和用户ID获取提交结果
     */
    @Override
    public List<SubmissionResultVO> getSubmissionResult(Integer problemId, Integer userId) {
        return submissionMapper.getSubmissionResult(problemId, userId);
    }

    /**
     * 获取所有提交结果
     */
    @Override
    public List<SubmissionResultVO> getAllSubmissionResult() {
        return submissionMapper.getAllSubmissionResult();
    }

    /**
     * 获取用户提交结果
     */
    @Override
    public List<SubmissionResultVO> getUserSubmissionResult(Integer userId) {
        return submissionMapper.getSubmissionInfoByUserId(userId);
    }

    /**
     * 通过题目ID删除提交结果
     */
    @Override
    public int deleteByProblemId(Integer id) {
        return submissionMapper.deleteByProblemId(id);
    }

    /**
     * 通过提交ID删除测试用例
     */
    @Override
    public int deleteTestCasesBySubmissionId(Integer id) {
        return submissionMapper.deleteTestCasesBySubmissionId(id);
    }

    @Override
    public List<Integer> getIdByProblemId(Integer id) {
        return submissionMapper.getIdByProblemId(id);
    }
}
