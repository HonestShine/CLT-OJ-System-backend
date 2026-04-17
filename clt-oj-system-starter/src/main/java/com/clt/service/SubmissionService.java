package com.clt.service;

import com.clt.dto.SubmissionCodeDTO;
import com.clt.entity.JudgeResult;
import com.clt.vo.SubmissionResultVO;

import java.util.List;

public interface SubmissionService {

    /**
     * 通过题目ID获取提交状态列表
     */
    List<String> getStatusByProblemId(Integer id);

    /**
     * 提交代码
     */
    SubmissionResultVO submitCode(SubmissionCodeDTO submissionCodeDTO, Integer userId) throws Exception;

    /**
     * 获取提交结果详情
     */
    JudgeResult getSubmissionResult(Integer id);

    /**
     * 通过题目ID和用户ID获取提交结果
     */
    List<SubmissionResultVO> getSubmissionResult(Integer problemId, Integer userId);

    /**
     * 获取所有提交结果
     */
    List<SubmissionResultVO> getAllSubmissionResult();

    /**
     * 获取用户提交结果
     */
    List<SubmissionResultVO> getUserSubmissionResult(Integer userId);

    /**
     * 删除题目下的所有提交结果
     */
    int deleteByProblemId(Integer id);

    /**
     * 删除题目下的所有测试用例
     */
    int deleteTestCasesBySubmissionId(Integer id);

    /**
     * 通过题目ID获取提交结果ID
     */
    List<Integer> getIdByProblemId(Integer id);
}
