package com.clt.service;

import com.clt.entity.Tag;

import java.util.List;

public interface TagService {

    /**
     * 添加标签
     */
    boolean insert(Tag tag);

    /**
     * 通过题目ID删除标签
     */
    int deleteByProblemId(Integer id);

    /**
     * 通过题目ID获取标签列表
     */
    List<Tag> getProblemTagListByProblemId(Integer id);
}
