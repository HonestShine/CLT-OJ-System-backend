package com.clt;

import com.clt.entity.Comment;
import com.clt.entity.Result;
import com.clt.entity.Solution;
import com.clt.service.CommentService;
import com.clt.service.SolutionService;
import com.clt.vo.CommentResultVO;
import com.clt.vo.SolutionResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/solutions")
@RestController
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取所有解决方案
     */
    @GetMapping
    public Result getAllSolutions() {
        List<SolutionResultVO> solutions = solutionService.getAllSolutions();
        if (solutions == null|| solutions.isEmpty()) {
            return Result.error("404", "无任何解决方案");
        }
        return Result.success(solutions);
    }

    /**
     * 创建解决方案
     */
    @PostMapping
    public Result createSolution(@RequestBody Solution solution) {
        Solution createdSolution;
        try {
            createdSolution = solutionService.createSolution(solution);
        } catch (RuntimeException e) {
            return Result.error( "500", e.getMessage());
        }
        return createdSolution != null ? Result.success(createdSolution) : Result.error( "500", "创建解决方案失败");
    }

    /**
     * 删除解决方案
     */
    @DeleteMapping("/{id}")
    public Result deleteSolution(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean deleted = solutionService.deleteSolution(id);
        return deleted ? Result.success() : Result.error("500", "删除解决方案失败");
    }

    /**
     * 修改解决方案
     */
    @PutMapping
    public Result updateSolution(@RequestBody Solution solution) {
        Solution updatedSolution = solutionService.updateSolution(solution);
        return updatedSolution != null ? Result.success(updatedSolution) : Result.error( "500", "修改解决方案失败");
    }

    /**
     * 点赞题解
     */
    @PutMapping("/likeSolution/{id}")
    public Result likeSolution(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean liked = solutionService.likeSolution(id);
        return liked ? Result.success() : Result.error("500", "点赞失败");
    }

    /**
     * 点赞评论
     */
    @PutMapping("/likeComment/{id}")
    public Result likeComment(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean liked = commentService.likeComment(id);
        return liked ? Result.success() : Result.error("500", "点赞失败");
    }

    /**
     * 评论题解
     */
    @PostMapping("/commentSolution")
    public Result commentSolution(@RequestBody Comment comment) {
        Comment commentSolution = commentService.commentSolution(comment);
        return commentSolution != null ? Result.success(commentSolution) : Result.error("500", "评论失败");
    }

    /**
     * 评论评论
     */
    @PostMapping("/commentComment")
    public Result commentComment(@RequestBody Comment comment) {
        Comment commentComment = commentService.commentComment(comment);
        return commentComment != null ? Result.success(commentComment) : Result.error("500", "评论失败");
    }

    /**
     * 获取题解评论
     */
    @GetMapping("/getSolutionComments/{id}")
    public Result getSolutionComments(@PathVariable Integer id) {
        List<CommentResultVO> comments = commentService.getCommentsBySolutionId(id);
        return comments != null ? Result.success(comments) : Result.error("404", "无任何评论");
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/deleteComment/{solutionId}/{commentId}")
    public Result deleteComment(@PathVariable Integer solutionId, @PathVariable Integer commentId) {
        if (solutionId == null || solutionId <= 0 || commentId == null || commentId <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean deleted = commentService.deleteComment(solutionId, commentId);
        return deleted ? Result.success() : Result.error("500", "删除评论失败");
    }

    /**
     * 取消题解点赞
     */
    @PutMapping("/cancelLikeSolution/{id}")
    public Result cancelLikeSolution(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean canceled = solutionService.cancelLikeSolution(id);
        return canceled ? Result.success() : Result.error("500", "取消点赞失败");
    }

    /**
     * 取消评论点赞
     */
    @PutMapping("/cancelLikeComment/{id}")
    public Result cancelLikeComment(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("400", "无效的ID");
        }

        boolean canceled = commentService.cancelLikeComment(id);
        return canceled ? Result.success() : Result.error("500", "取消点赞失败");
    }

}
