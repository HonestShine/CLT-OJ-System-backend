package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemSample {
    private Integer id; //编号
    private Integer problemId;  //题目编号
    private Integer sampleOrder;    //样例序号
    private String inputContent;    //输入样例
    private String outputContent;   //输出样例
    private Integer isExample;  //是否展出
}
