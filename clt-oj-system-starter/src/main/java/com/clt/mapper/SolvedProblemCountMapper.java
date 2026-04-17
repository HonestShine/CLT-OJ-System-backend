package com.clt.mapper;

import com.clt.entity.SolvedProblemCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SolvedProblemCountMapper {

    /**
     * 通过用户ID查询已解决题目数表ID
     */
    SolvedProblemCount getIdByUserIdAndProblemId(Integer userId, Integer problemId);

    /**
     * 插入已解决题目数和提交数
     */
    void insert(SolvedProblemCount solvedProblemCount);

    /**
     * 更新已解决题目数
     */
    void updateSolvedCount(Integer userId, Integer problemId);

    /**
     * 通过ID查询已解决题目数
     */
    Integer getSolvedCount(Integer id);

    /**
     * 通过ID查询提交数
     */
    Integer getSubmissionCount(Integer id);

    int getUserSolvedCount(@Param("userId") Integer userId, @Param("id") Integer id);
}
