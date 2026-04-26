package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResultVO {
    private Integer id;
    private Integer problemId;
    private Integer userId;
    private String code;
    private Double runtime;
    private Double memory;
    private String language;
    private String status;
    private LocalDateTime submitTime;
}
