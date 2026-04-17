package com.clt.mapper;

import com.clt.entity.Solution;
import com.clt.vo.SolutionResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SolutionMapper {

    /**
     * 获取所有解决方案
     */
    List<SolutionResultVO> getAllSolutions();

    /**
     * 通过题目ID获取题目标题
     */
    String getProblemTitleById(@Param("id") Integer id);

    /**
     * 创建解决方案
     */
    Integer createSolution(@Param("solution") Solution solution);

    /**
     * 创建官方题解
     */
    Integer createSolutionOfOfficial(@Param("solution") Solution solution);

    /**
     * 创建解决方案内容
     */
    Integer createSolutionContent(@Param("solution") Solution solution);

    /**
     * 根据 id 获取解决方案
     */
    Solution getSolutionById(@Param("id") Integer id);

    /**
     * 根据 id 删除解决方案
     */
    int deleteSolution(@Param("id") Integer id);

    /**
     * 根据 id 删除解决方案内容
     */
    int deleteSolutionContent(@Param("id") Integer id);

    /**
     * 根据 id 更新解决方案
     */
    int updateSolution(@Param("solution") Solution solution);

    /**
     * 根据 id 更新解决方案内容
     */
    int updateSolutionContent(@Param("solution") Solution solution);

    /**
     * 根据 id 点赞解决方案
     */
    int likeSolution(@Param("id") Integer id);

    /**
     * 追加评论次数
     */
    void commentSolution(@Param("solutionId") Integer solutionId);

    /**
     * 减去评论次数
     */
    int subtractCommentNumber(@Param("solutionId") Integer solutionId);

    /**
     * 取消点赞解决方案
     */
    int cancelLikeSolution(@Param("id") Integer id);
}
