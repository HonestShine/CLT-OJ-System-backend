package com.clt.vo;


import com.clt.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemTitleInfoVO {
    private Integer problemId;  //题目编号
    private String title;   //题目
    private String difficulty; //难度
    private String passRate;    //通过率
    private List<Tag> tags; //标签
}
