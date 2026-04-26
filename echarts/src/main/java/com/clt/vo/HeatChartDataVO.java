package com.clt.vo;

import com.clt.entity.HeatChartData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatChartDataVO {
    private List<HeatChartData> dataList;
    private Integer totalThisYear;
    private Integer streakDays;
}
