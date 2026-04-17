package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution {
    private Integer id;
    private Integer problemId;
    private Integer userId;
    private String title;
    private String language;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isOfficial;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String contentMd;
}
