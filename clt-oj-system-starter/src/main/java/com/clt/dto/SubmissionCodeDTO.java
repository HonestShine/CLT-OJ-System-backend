package com.clt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmissionCodeDTO {
    @NotBlank(message = "题目ID不能为空")
    private Integer problemId;
    @NotBlank(message = "代码不能为空")
    private String code;
    @NotBlank(message = "语言不能为空")
    private String language;
}
