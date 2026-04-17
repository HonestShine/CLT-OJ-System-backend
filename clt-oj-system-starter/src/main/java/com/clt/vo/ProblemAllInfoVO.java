package com.clt.vo;

import com.clt.entity.ProblemSample;
import com.clt.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemAllInfoVO {
    private Integer id;  //编号
    private String title;       //题目
    private String description; //题目描述
    private String inputFormat; //输入格式
    private String outputFormat;    //输出格式
    private Double timeLimit;  //时间限制
    private Double memoryLimit;    //内存限制
    private String difficulty;  //难度
    private String hint;    //提示
    private List<Tag> tags; //标签
    private List<ProblemSample> samples;    //样例
}
