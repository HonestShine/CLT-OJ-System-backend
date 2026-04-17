package com.clt.mapper;

import com.clt.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    /**
     * 添加标签
     */
    int insert(Tag tag);

    /**
     * 通过题目ID删除标签
     */
    int deleteByProblemId(Integer id);

    /**
     * 通过题目ID获取标签列表
     */
    List<Tag> getProblemTagListByProblemId(@Param("id") Integer id);
}
