package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunchVO {
    private Integer id;
    private LocalDateTime punchDate;
    private Integer numberOfPunchOuts;
}
