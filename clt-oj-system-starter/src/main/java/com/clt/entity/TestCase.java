package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
    private Integer id;
    private Integer submissionId;
    private Integer caseId;
    private String status;
    private Double runtime;
    private Double memory;
    private LocalDateTime updateTime;
}
