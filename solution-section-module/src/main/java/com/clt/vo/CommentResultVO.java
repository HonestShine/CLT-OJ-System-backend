package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResultVO {
    private Integer id;
    private Integer solutionId;
    private Integer userId;
    private String nickname;
    private String avatar;
    private Integer parentId;
    private String content;
    private Integer likeCount;
    private LocalDateTime createTime;
}
