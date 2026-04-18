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
    int insert(@Param("tag") Tag tag);

    /**
     * 通过题目ID删除标签
     */
    int deleteByProblemId(@Param("id") Integer id);

    /**
     * 通过题目ID获取标签列表
     */
    List<Tag> getProblemTagListByProblemId(@Param("id") Integer id);

    /**
     * 判断标签是否存在
     */
    int isExist(@Param("name") String name);

    /**
     * 添加标签关系
     */
    int insertRelationship(@Param("tag") Tag tag);

    /**
     * 设置标签颜色
     */
    int setTagColor(@Param("tag") Tag tag);

    /**
     * 通过标签名获取标签ID
     */
    Integer getIdByName(@Param("name") String name);
}
