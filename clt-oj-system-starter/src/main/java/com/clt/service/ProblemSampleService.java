package com.clt.service;

import com.clt.entity.ProblemSample;
import java.util.List;

public interface ProblemSampleService {

    /**
     * 通过题目ID获取例题列表
     */
    List<ProblemSample> getProblemSampleListByProblemIdIfVisible(Integer id);

    /**
     * 获取题目样例列表
     */
    List<ProblemSample> getProblemSampleList();

    /**
     * 添加题目样例
     */
    boolean insert(ProblemSample sample);

    /**
     * 通过题目ID删除样例
     */
    int deleteByProblemId(Integer id);
}
