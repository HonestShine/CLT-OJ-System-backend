package com.clt.service.impl;

import com.clt.mapper.TagMapper;
import com.clt.entity.Tag;
import com.clt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 添加标签
     */
    @Override
    public boolean insert(Tag tag) {
        return tagMapper.insert(tag) > 0;
    }

    /**
     * 通过题目ID删除标签
     */
    @Override
    public int deleteByProblemId(Integer id) {
      return tagMapper.deleteByProblemId(id);
    }

    /**
     * 通过题目ID获取标签列表
     */
    @Override
    public List<Tag> getProblemTagListByProblemId(Integer id) {
        return tagMapper.getProblemTagListByProblemId(id);
    }
}
