package com.clt.service.impl;

import com.clt.entity.HeatChartData;
import com.clt.mapper.EChartsMapper;
import com.clt.service.EChartsService;
import com.clt.vo.HeatChartDataVO;
import com.clt.vo.RadarChartDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class EChartsServiceImpl implements EChartsService {

    @Autowired
    private EChartsMapper eChartsMapper;

    /**
     * 雷达图数据
     */
    @Override
    public RadarChartDataVO getRadarChartData(Integer userId) {
        RadarChartDataVO result = new RadarChartDataVO();
        String[] dimensions = {"算法", "数据结构", "动态规划", "贪心", "图论", "字符串处理"};
        Integer[] values = {(int) ((getMolecule("算法", userId) + getMolecule("查找", userId) + getMolecule("排序", userId) + getMolecule("递归", userId) + getMolecule("DFS", userId)) * 100.0 / (getDenominator("算法", userId) + + getDenominator("查找", userId) + getDenominator("排序", userId) + getDenominator("递归", userId) + getDenominator("DFS", userId))),
                            (int) ((getMolecule("数据结构", userId) + getMolecule("链表", userId) + getMolecule("树", userId) + getMolecule("矩阵", userId) + getMolecule("堆", userId) + getMolecule("队列", userId) + getMolecule("栈", userId) + getMolecule("数组", userId)) * 100.0 / (getDenominator("数据结构", userId) + + getDenominator("链表", userId) + getDenominator("树", userId) + getDenominator("矩阵", userId) + getDenominator("堆", userId) + getDenominator("队列", userId) + getDenominator("栈", userId) + getDenominator("数组", userId))),
                            (int) (getMolecule("动态规划", userId) * 100.0 / getDenominator("动态规划", userId)),
                            (int) (getMolecule("贪心", userId) * 100.0 / getDenominator("贪心", userId)),
                            (int) (getMolecule("图", userId) * 100.0 / getDenominator("图", userId)),
                            (int) (getMolecule("字符串", userId) * 100.0 / getDenominator("字符串", userId))};
        result.setDimensions(Arrays.asList(dimensions));
        result.setValues(Arrays.asList(values));
        return result;
    }

    /**
     * 热力图数据
     */
    @Override
    public HeatChartDataVO getHeatChartData(Integer userId) {
        List<HeatChartData> heatChartData = eChartsMapper.getHeatChartData(userId);
        Integer totalThisYear = eChartsMapper.getTotalThisYear(userId);
        checkPunch(userId);
        Integer streakDays = eChartsMapper.getStreakDays(userId);

        return new HeatChartDataVO(heatChartData, totalThisYear, streakDays);
    }

    private Integer getMolecule(String condition, Integer userId) {
        return eChartsMapper.getScoreToRadarChart(condition, userId);
    }

    private Integer getDenominator(String condition, Integer userId) {
        return eChartsMapper.getTotalScoreToRadarChart(condition, userId);
    }

    /**
     * 检查打卡
     */
    public void checkPunch(Integer userId) {
        LocalDate leastSubmissionDate = eChartsMapper.getLastSubmissionTime(userId).toLocalDate();
        // 如果不同年 或 如果相差天数大于1
        if (leastSubmissionDate.getYear() != LocalDate.now().getYear() || ChronoUnit.DAYS.between(leastSubmissionDate, LocalDate.now()) > 1) {
            // 清0
            eChartsMapper.cleanPunchCount(userId);
        }
    }
}
