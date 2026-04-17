package com.clt.service.impl;

import com.clt.entity.Comment;
import com.clt.mapper.CommentMapper;
import com.clt.mapper.SolutionMapper;
import com.clt.service.CommentService;
import com.clt.vo.CommentResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SolutionMapper solutionMapper;

    /**
     * 评论解决方案
     */
    @Transactional
    @Override
    public Comment commentSolution(Comment comment) {
        commentMapper.createCommentToSolution(comment);
        if (comment.getId() == null) {
            return null;
        }
        //追加评论次数
        solutionMapper.commentSolution(comment.getSolutionId());
        return commentMapper.getCommentById(comment.getId());
    }

    /**
     * 获取解决方案的评论
     */
    @Override
    public List<CommentResultVO> getCommentsBySolutionId(Integer solutionId) {
        return commentMapper.getCommentsBySolutionId(solutionId);
    }

    /**
     * 点赞
     */
    @Override
    public boolean likeComment(Integer id) {
        return commentMapper.likeComment(id) > 0;
    }

    /**
     * 评论评论
     */
    @Transactional
    @Override
    public Comment commentComment(Comment comment) {
        if (comment.getParentId() == null || comment.getSolutionId() == null) {
            return null;
        }
        commentMapper.createCommentToComment(comment);
        if (comment.getId() == null) {
            return null;
        }
        //追加评论次数
        solutionMapper.commentSolution(comment.getSolutionId());
        return commentMapper.getCommentById(comment.getId());
    }

    /**
     * 删除评论
     */
    @Transactional
    @Override
    public boolean deleteComment(Integer solutionId, Integer commentId) {
        return solutionMapper.subtractCommentNumber(solutionId) >= 0 && commentMapper.deleteComment(commentId) > 0;
    }

    /**
     * 取消点赞
     */
    @Override
    public boolean cancelLikeComment(Integer id) {
        return commentMapper.cancelLikeComment(id) > 0;
    }

}
