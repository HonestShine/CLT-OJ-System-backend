package com.clt.service;

import com.clt.vo.HeatChartDataVO;
import com.clt.vo.RadarChartDataVO;

public interface EChartsService {
    /**
     * 雷达图数据
     */
    RadarChartDataVO getRadarChartData(Integer userId);

    /**
     * 热力图数据
     */
    HeatChartDataVO getHeatChartData(Integer userId);
}
