package com.clt.mapper;

import com.clt.entity.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemMapper {

    /**
     * 获取题目列表
     */
    List<Problem> getProblemTilesList();

    /**
     * 通过题目ID获取题目详情
     */
    Problem getProblemById(Integer id);

    /**
     * 添加问题
     */
    Integer insert(Problem problem);

    /**
     * 通过标题和描述获取题目ID
     */
    Integer getIdByTitleAndDescription(String title, String description);

    /**
     * 更新问题
     */
    int update(Problem problem);

    /**
     * 搜索问题
     */
    List<Problem> searchProblemList(String keyword);

    /**
     * 通过ID删除问题
     */
    int deleteById(Integer id);

    /**
     * 分页查询
     */
    List<Problem> getProblemTilesListOfPage(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     * 获取题目总数
     */
    Integer getProblemListCount();

    List<Problem> getProblemTilesListByDifficultyOfPage(@Param("difficulty") Integer difficulty, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    List<Problem> getRecommendProblem();

    List<Integer> getSolutionIdByProblemId(@Param("id") Integer id);

    int deleteSolutionContentBySolutionId(@Param("solutionId") Integer solutionId);

    int deleteSolutionCommentBySolutionId(@Param("solutionId") Integer solutionId);

    int deleteSolutionByProblemId(@Param("id") Integer id);

    int deleteUserProblemStatusByProblemId(@Param("id") Integer id);

    int deleteSolvedProblemCountByProblemId(@Param("id") Integer id);

    Integer getFilteredProblemCount(@Param("difficulty") int difficulty);

    Integer getProblemByKeywordCount(@Param("keyword") String keyword, @Param("start") Integer start, @Param("pageSize") Integer pageSize);
}
