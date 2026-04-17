package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolvedProblemCount {
    private Integer id;
    private Integer userId;
    private Integer problemId;
    private Boolean solvedCount = false;
}
