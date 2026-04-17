package com.clt.service;

import com.clt.entity.Comment;
import com.clt.vo.CommentResultVO;

import java.util.List;

public interface CommentService {

    /**
     * 评论解决方案
     */
    Comment commentSolution(Comment comment);

    /**
     * 获取解决方案的评论
     */
    List<CommentResultVO> getCommentsBySolutionId(Integer solutionId);

    /**
     * 点赞评论
     */
    boolean likeComment(Integer id);

    /**
     * 评论评论
     */
    Comment commentComment(Comment comment);

    /**
     * 删除评论
     */
    boolean deleteComment(Integer solutionId, Integer commentId);

    /**
     * 取消点赞
     */
    boolean cancelLikeComment(Integer id);
}
