package com.clt.mapper;

import com.clt.entity.JudgeResult;
import com.clt.entity.TestCase;
import com.clt.vo.SubmissionResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SubmissionMapper {

    /**
     * 通过题目ID获取提交状态
     */
    List<String> getStatusByProblemId(Integer id);

    /**
     * 插入提交记录
     */
    void insertSubmission(JudgeResult judgeResult);

    /**
     * 插入测试用例
     */
    void insertTestCase(TestCase testCase);

    /**
     * 通过提交ID获取提交信息
     */
    JudgeResult getSubmissionInfoById(Integer id);

    /**
     * 通过提交ID获取测试用例
     */
    List<TestCase> getSubmissionTestCasesBYSubmissionId(Integer id);

    /**
     * 通过题目ID和用户ID获取提交结果
     */
    List<SubmissionResultVO> getSubmissionResult(Integer problemId, Integer userId);

    /**
     * 获取所有提交结果
     */
    List<SubmissionResultVO> getAllSubmissionResult();

    /**
     * 获取用户最后一次提交时间
     */
    LocalDateTime getLastSubmissionTime(Integer id);

    List<SubmissionResultVO> getSubmissionInfoByUserId(Integer userId);

    /**
     * 通过题目ID删除提交记录
     */
    int deleteByProblemId(Integer id);

    /**
     * 通过题目ID获取提交ID
     */
    List<Integer> getIdByProblemId(@Param("id") Integer id);

    /**
     * 通过提交ID删除测试用例
     */
    int deleteTestCasesBySubmissionId(@Param("id") Integer id);
}
