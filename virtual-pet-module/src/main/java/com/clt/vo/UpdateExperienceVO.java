package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExperienceVO {
    private Integer id;
    private BigInteger experience;
    private Integer level;
}
