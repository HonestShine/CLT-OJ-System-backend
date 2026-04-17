package com.clt.mapper;

import com.clt.entity.ProblemSample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemSampleMapper {

    /**
     * 通过题目ID获取例题列表
     */
    List<ProblemSample> getProblemSampleListByProblemId(Integer id);

    /**
     * 获取题目样例列表
     */
    List<ProblemSample> getProblemSampleList();

    /**
     * 添加题目样例
     */
    Integer insert(ProblemSample sample);

    /**
     * 通过题目ID删除题目样例
     */
    int deleteByProblemId(Integer id);
}
