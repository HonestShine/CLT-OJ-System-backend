package com.clt.service.impl;

import com.clt.mapper.ProblemSampleMapper;
import com.clt.entity.ProblemSample;
import com.clt.service.ProblemSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemSampleServiceImpl implements ProblemSampleService {

    @Autowired
    private ProblemSampleMapper problemSampleMapper;

    /**
     * 通过题目ID获取例题列表
     */
    @Override
    public List<ProblemSample> getProblemSampleListByProblemIdIfVisible(Integer id) {
        return problemSampleMapper.getProblemSampleListByProblemId(id);
    }

    /**
     * 获取题目样例列表
     */
    @Override
    public List<ProblemSample> getProblemSampleList() {
        return problemSampleMapper.getProblemSampleList();
    }

    /**
     * 添加题目样例
     */
    @Override
    public boolean insert(ProblemSample sample) {
        return problemSampleMapper.insert(sample) > 0;
    }

    /**
     * 通过题目ID删除样例
     */
    @Override
    public int deleteByProblemId(Integer id) {
        return problemSampleMapper.deleteByProblemId(id);
    }
}
