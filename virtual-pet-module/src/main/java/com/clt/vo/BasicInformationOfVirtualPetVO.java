package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicInformationOfVirtualPetVO {
    private Integer id;
    private Integer userId;
    private String name;
    private BigInteger experience;
    private Integer level;
    private LocalDateTime createdAt;
    private LocalDateTime punchDate;
    private Integer numberOfPunchOuts;
}
