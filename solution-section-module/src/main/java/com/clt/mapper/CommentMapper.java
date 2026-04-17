package com.clt.mapper;

import com.clt.entity.Comment;
import com.clt.vo.CommentResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    /**
     * 创建评论
     */
    void createCommentToSolution(@Param("comment") Comment comment);

    /**
     * 获取解决方案的评论
     */
    List<CommentResultVO> getCommentsBySolutionId(@Param("solutionId") Integer solutionId);

    /**
     * 根据 id 获取评论
     */
    Comment getCommentById(@Param("id") Integer id);

    /**
     * 点赞评论
     */
    int likeComment(@Param("id") Integer id);

    /**
     * 创建评论
     */
    void createCommentToComment(@Param("comment") Comment comment);

    /**
     * 删除解决方案下的所有评论
     */
    int deleteCommentsBySolutionId(@Param("solutionId") Integer solutionId);

    /**
     * 删除评论
     */
    int deleteComment(@Param("id") Integer id);

    /**
     * 取消点赞评论
     */
    int cancelLikeComment(@Param("id") Integer id);
}
