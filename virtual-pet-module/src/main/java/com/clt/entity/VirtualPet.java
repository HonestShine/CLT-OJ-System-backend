package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualPet {
    private Integer id;
    private Integer userId;
    private String name;
    private BigInteger experience;
    private Integer level;
    private LocalDateTime createdAt;
    private LocalDateTime punchDate;
    private Integer numberOfPunchOuts;
    private List<String> softWords;
    private List<String> viciousWords;
}
