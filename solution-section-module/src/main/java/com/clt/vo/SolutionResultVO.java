package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionResultVO {
    private Integer id;
    private Integer problemId;
    private String problemTitle;
    private Integer userId;
    private String nickname;
    private String avatar;
    private String title;
    private String language;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isOfficial;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String contentMd;
}
