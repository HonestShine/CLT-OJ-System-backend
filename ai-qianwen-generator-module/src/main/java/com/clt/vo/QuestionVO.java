package com.clt.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * AI 题目生成接口的响应 DTO (Data Transfer Object)
 * 用于映射最终返回给前端的 JSON 结构。
 */
@Data // Lombok 注解，自动生成 getter/setter
public class QuestionVO {

    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String timeLimit;
    private String memoryLimit;
    private String difficulty;
    private String hint;
    private List<Tag> tags;
    private List<Sample> samples;

    /**
     * 题目样例内部类
     * 用于表示输入输出样例。
     */
    @Data
    public static class Sample {
        @JsonProperty("sampleOrder")
        private Integer sampleOrder;    //样例序号
        @JsonProperty("inputContent") // 明确指定 JSON 键名
        private String inputContent;
        @JsonProperty("outputContent") // 明确指定 JSON 键名
        private String outputContent;
        @JsonProperty("isExample")
        private Integer isExample;  //是否展出
    }

    @Data
    public static class Tag {
        @JsonProperty("name")
        private String name;    //标签名称
        @JsonProperty("color")
        private String color;   //标签颜色
    }
}