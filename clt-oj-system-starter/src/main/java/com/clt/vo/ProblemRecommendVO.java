package com.clt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRecommendVO implements Comparable<ProblemRecommendVO>{
    private Integer problemId;
    private String problemTitle;
    private String description;
    private String difficulty;
    private String passRate;

    @Override
    public int compareTo(@NotNull ProblemRecommendVO o) {
        return this.getPassRate().compareTo(o.getPassRate());
    }
}
