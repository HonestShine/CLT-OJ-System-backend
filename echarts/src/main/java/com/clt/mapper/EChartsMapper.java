package com.clt.mapper;

import com.clt.entity.HeatChartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface EChartsMapper {

    /**
     * 根据条件获取实际解题数
     */
    Integer getScoreToRadarChart(@Param("condition") String condition, @Param("userId") Integer userId);

    /**
     * 根据条件获取总解题数
     */
    Integer getTotalScoreToRadarChart(@Param("condition") String condition, @Param("userId") Integer userId);

    /**
     * 获取热力图数据
     */
    List<HeatChartData> getHeatChartData(@Param("userId") Integer userId);

    /**
     * 获取今年总提交数
     */
    Integer getTotalThisYear(@Param("userId") Integer userId);

    /**
     * 获取当前连续天数
     */
    Integer getStreakDays(@Param("userId") Integer userId);

    OffsetDateTime getLastSubmissionTime(@Param("userId") Integer userId);

    void cleanPunchCount(@Param("userId") Integer userId);
}
