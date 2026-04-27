package com.clt.controller;

import com.clt.entity.Result;
import com.clt.service.EChartsService;
import com.clt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echarts")
public class EChartsController {

    @Autowired
    private EChartsService eChartsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 雷达图
     */
    @GetMapping("/radarChart")
    public Result getRadarChartData(@RequestHeader("token") String token) {
        if (!jwtUtil.validateToken(token) || token == null) {
            return Result.error("401", "未登录");
        }
        // 从token中获取用户ID
        Integer id = jwtUtil.parseToken(token).get("id", Integer.class);

        return Result.success(eChartsService.getRadarChartData(id));
    }

    /**
     * 热力图
     */
    @GetMapping("/heatChart")
    public Result getHeatChartData(@RequestHeader("token") String token) {
        if (!jwtUtil.validateToken(token) || token == null) {
            return Result.error("401", "未登录");
        }
        // 从token中获取用户ID
        Integer id = jwtUtil.parseToken(token).get("id", Integer.class);

        return Result.success(eChartsService.getHeatChartData(id));
    }

}
