package com.clt.service;

import com.clt.dto.ProblemCreationOrUpdateDTO;
import com.clt.entity.Problem;
import com.clt.exception.NoProblemException;
import com.clt.vo.ProblemPageVO;
import com.clt.vo.ProblemRecommendVO;

import java.util.List;

public interface ProblemService {

    /**
     * 通过题目ID获取题目详情
     */
    Problem getProblemById(Integer id) throws NoProblemException;

    /**
     * 添加问题
     */
    Problem CreateProblem(ProblemCreationOrUpdateDTO dto) throws RuntimeException;


    /**
     * 更新问题
     */
    Problem UpdateProblem(ProblemCreationOrUpdateDTO dto) throws RuntimeException;

    /**
     * 搜索题目
     */
    ProblemPageVO searchProblem(String keyword, Integer start, Integer pageSize);

    /**
     * 根据难度过滤题目
     */
    ProblemPageVO filterProblem(String difficulty, Integer start, Integer pageSize);

    /**
     * 删除问题
     */
    boolean deleteProblem(Integer id) throws RuntimeException;

    /**
     * 分页获取题目列表
     */
    ProblemPageVO getProblemListOfPage(Integer start, Integer pageeSize);

    /**
     * 获取推荐题目
     */
    List<ProblemRecommendVO> getRecommendProblems();

}
