package com.clt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer solutionId;
    private Integer userId;
    private Integer parentId;
    private String content;
    private Integer likeCount;
    private LocalDateTime createTime;
}
