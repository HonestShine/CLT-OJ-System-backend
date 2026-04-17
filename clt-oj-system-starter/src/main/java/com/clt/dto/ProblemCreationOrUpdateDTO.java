package com.clt.dto;

import com.clt.entity.ProblemSample;
import com.clt.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProblemCreationOrUpdateDTO {
    private Integer id;
    private String title;       //题目
    private String description; //题目描述
    private String inputFormat; //输入格式
    private String outputFormat;    //输出格式
    private Double timeLimit;  //时间限制
    private Double memoryLimit;    //内存限制
    private Integer difficulty;  //难度
    private String hint;    //提示
    private List<Tag> tags;  //标签
    private List<ProblemSample> samples;  //测试用例

}
