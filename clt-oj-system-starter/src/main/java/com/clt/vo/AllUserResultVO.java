package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserResultVO {
    private Integer id;
    private String nickname;
    private Integer solvedCount;
    private Integer submissionCount;
    private LocalDateTime lastSubmitTime;
}
