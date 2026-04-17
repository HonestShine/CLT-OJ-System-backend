package com.clt.service;

import com.clt.entity.Comment;
import com.clt.entity.Solution;
import com.clt.vo.SolutionResultVO;

import java.util.List;

public interface SolutionService {

    /**
     * 获取所有解决方案
     */
    List<SolutionResultVO> getAllSolutions();

    /**
     * 创建解决方案
     */
    Solution createSolution(Solution solution) throws RuntimeException;

    /**
     * 删除解决方案
     */
    boolean deleteSolution(Integer id);

    /**
     * 更新解决方案
     */
    Solution updateSolution(Solution solution);

    /**
     * 点赞解决方案
     */
    boolean likeSolution(Integer id);

    /**
     * 取消点赞解决方案
     */
    boolean cancelLikeSolution(Integer id);
}
