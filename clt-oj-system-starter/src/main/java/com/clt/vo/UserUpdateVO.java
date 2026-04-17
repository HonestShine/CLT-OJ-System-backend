package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateVO {
    private Integer id;
    private String nickname;
    private String hobby;
    private String avatar;
    private String introduction;
    private Integer solvedCount;
    private Integer submissionCount;
    private LocalDateTime lastSubmissionTime;
}
