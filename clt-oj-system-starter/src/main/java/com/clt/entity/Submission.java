package com.clt.entity;

import com.clt.enums.SubmissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    private Integer id;  //编号
    private Integer userId;     //用户编号
    private Integer problemID;  //问题编号
    private String language;    //编程语言类型
    private String code;    //提交的代码
    private Integer status;    //提交结果状态
    private String resultMessage;   //提交结果信息
    private LocalDateTime submitTime;   //提交时间
    private Double runtime;
    private Double memory;
}
