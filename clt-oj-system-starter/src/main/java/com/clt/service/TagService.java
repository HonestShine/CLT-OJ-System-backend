package com.clt.service;

import com.clt.entity.Tag;

import java.util.List;

public interface TagService {

    /**
     * 添加标签
     */
    boolean insert(Tag tag);

    /**
     * 判断标签是否存在
     */
    boolean isExist(String name);

    /**
     * 通过题目ID删除标签
     */
    int deleteByProblemId(Integer id);

    /**
     * 通过题目ID获取标签列表
     */
    List<Tag> getProblemTagListByProblemId(Integer id);

    /**
     * 添加标签关系
     */
    boolean insertRelationship(Tag tag);

    /**
     * 设置标签颜色
     */
    boolean setTagColor(Tag tag);

    /**
     * 通过标签名获取标签ID
     */
    Integer getIdByName(String name);
}
