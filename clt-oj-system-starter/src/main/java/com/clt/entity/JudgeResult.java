package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    private Integer id;
    private Integer problemId;
    private Integer userId;
    private String code;
    private String language;
    private String status;
    private String stdout;
    private String stderr;
    private String compileOutput;
    private Double runtime;
    private Double memory;
    private String message;
    private LocalDateTime submitTime;
    private List<TestCase> testResults = new ArrayList<>();
}
