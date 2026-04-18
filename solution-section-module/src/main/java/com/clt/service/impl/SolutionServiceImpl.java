package com.clt.service.impl;

import com.clt.entity.Solution;
import com.clt.mapper.CommentMapper;
import com.clt.mapper.SolutionMapper;
import com.clt.service.SolutionService;
import com.clt.vo.SolutionResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionServiceImpl implements SolutionService {

    @Autowired
    private SolutionMapper solutionMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 获取所有解决方案
     */
    @Override
    public List<SolutionResultVO> getAllSolutions() {
        List<SolutionResultVO> solutionResultVOS = solutionMapper.getAllSolutions();
        for (SolutionResultVO solutionResultVO : solutionResultVOS) {
            solutionResultVO.setProblemTitle(solutionMapper.getProblemTitleById(solutionResultVO.getProblemId()));
        }
        return solutionResultVOS;
    }

    /**
     * 创建解决方案
     */
    @Transactional
    @Override
    public Solution createSolution(Solution solution) throws RuntimeException {
        int rows;
        if (solution.getIsOfficial() == 1) {
            rows = solutionMapper.createSolutionOfOfficial(solution);
        }else {
            rows = solutionMapper.createSolution(solution);
        }

        if (rows <= 0 || solution.getId() == null) {
            throw new RuntimeException("创建解决方案失败");
        }
        int contentRows = solutionMapper.createSolutionContent(solution);
        if (contentRows <= 0) {
            throw new RuntimeException("创建解决方案内容失败");
        }

        return solutionMapper.getSolutionById(solution.getId());
    }

    /**
     * 删除解决方案
     */
    @Transactional
    @Override
    public boolean deleteSolution(Integer id) {
        return commentMapper.deleteCommentsBySolutionId(id) >= 0 && solutionMapper.deleteSolutionContent(id) >= 0 && solutionMapper.deleteSolution(id) > 0;
    }

    /**
     * 修改解决方案
     */
    @Transactional
    @Override
    public Solution updateSolution(Solution solution) {
        int rows = solutionMapper.updateSolution(solution);
        if (rows <= 0) {
            return null;
        }
        if (solution.getContentMd() != null) {
            solutionMapper.updateSolutionContent(solution);
        }
        return solutionMapper.getSolutionById(solution.getId());
    }

    /**
     * 点赞解决方案
     */
    @Override
    public boolean likeSolution(Integer id) {
        return solutionMapper.likeSolution(id) > 0;
    }

    /**
     * 取消点赞解决方案
     */
    @Override
    public boolean cancelLikeSolution(Integer id) {
        return solutionMapper.cancelLikeSolution(id) > 0;
    }

}
