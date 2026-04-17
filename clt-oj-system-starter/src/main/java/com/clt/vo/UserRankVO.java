package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRankVO {
    private Integer userId;
    private Integer rank;
    private String avatar;
    private String nickname;
    private  Integer solvedCount;
    private  Integer submissionCount;
    private  String acceptance;
}
