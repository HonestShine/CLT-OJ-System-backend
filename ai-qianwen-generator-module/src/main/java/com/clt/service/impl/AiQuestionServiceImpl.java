package com.clt.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.clt.service.AiQuestionService;
import com.clt.vo.QuestionVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.Collections;

/**
 * AI 题目生成服务类 (集成千问模型)
 * 调用千问 API 并生成结构化的题目数据。
 */
@Slf4j
@Service
public class AiQuestionServiceImpl implements AiQuestionService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Generation generation;

    @Value("${dashscope.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        this.generation = new Generation();
        System.setProperty("dashscope.api_key", apiKey);
    }

    /**
     * 根据用户提供的提示词生成题目数据
     * @param promptWord 用户输入的提示词
     * @return QuestionResponse 对象，包含生成的题目信息
     */

    @Override
    public QuestionVO generateQuestion(String promptWord) {
        try {
            String fullPrompt = buildPromptForQwen(promptWord);

            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(fullPrompt)
                    .build();

            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-max")
                    .messages(Collections.singletonList(userMsg))
                    .resultFormat("json_object")
                    .temperature(0.7f)
                    .topP(0.8)
                    .build();

            GenerationResult response = generation.call(param);

            log.info("千问 API 响应状态码: {}, 请求ID: {}", response.getStatusCode(), response.getRequestId());

            if (response.getOutput() == null) {
                log.error("千问 API 返回的 output 为 null");
                QuestionVO errorResp = new QuestionVO();
                errorResp.setTitle("生成失败");
                errorResp.setDescription("AI 服务返回空响应");
                return errorResp;
            }

            String generatedText = response.getOutput().getText();

            if (generatedText == null || generatedText.isEmpty()) {
                log.error("千问 API 返回的 text 为空, 完整响应: {}", response);
                QuestionVO errorResp = new QuestionVO();
                errorResp.setTitle("生成失败");
                errorResp.setDescription("AI 服务未返回有效内容");
                return errorResp;
            }

            log.info("千问返回的原始内容: {}", generatedText);

            return objectMapper.readValue(generatedText, QuestionVO.class);

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("调用千问 API 失败: ", e);
            QuestionVO errorResp = new QuestionVO();
            errorResp.setTitle("生成失败");
            errorResp.setDescription("AI 服务调用失败: " + e.getMessage());
            return errorResp;
        } catch (Exception e) {
            log.error("JSON 解析失败或返回格式错误: ", e);
            QuestionVO errorResp = new QuestionVO();
            errorResp.setTitle("生成失败");
            errorResp.setDescription("AI 返回格式错误: " + e.getMessage());
            return errorResp;
        }
    }

    /**
     * 构建发送给千问的完整提示词
     * @param promptWord 用户输入的提示词
     * @return 完整的 Prompt 字符串
     */
    private String buildPromptForQwen(String promptWord) {
        return "你是一位专业的编程题生成助手。请根据用户提供的主题关键词: \"" + promptWord + "\", 生成一道编程题。" +
                "请根据用户提供的主题关键词生成题目title，题目的描述description、输入格式inputFormat、输出格式outputFormat、" +
                "样例samples、提示hint、难度difficulty、时间限制timeLimit、内存限制memoryLimit和标签tags信息，" +
                "其中，timeLimit最大为20s；memoryLimit最大为2048MB；difficulty取值：简单、中等、困难。" +
                "samples中包含：样例序号sampleOrder、输入样例inputContent、输出样例outputContent、是否例举isExample，" +
                "其中isExample取值：1(展示)、0(不展示)" +
                "tags中包含：标签名称name、标签颜色color，" +
                "其中color使用十六进制RGB表示法返回，如红色：#FF0000" +
                "请严格按照以下 JSON 格式返回，不要包含任何其他解释文字，只返回纯 JSON 对象：" +
                "\n{\n" +
                "  \"title\": \"...\",\n" +
                "  \"description\": \"...\",\n" +
                "  \"inputFormat\": \"...\",\n" +
                "  \"outputFormat\": \"...\",\n" +
                "  \"timeLimit\": \"...s\",\n" +
                "  \"memoryLimit\": \"...MB\",\n" +
                "  \"difficulty\": \"...\",\n" +
                "  \"hint\": \"...\",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"name\": \"...\",\n" +
                "      \"color\": \"...\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"samples\": [\n" +
                "    {\n" +
                "      \"sampleOrder\": \"...\",\n" +
                "      \"inputContent\": \"...\",\n" +
                "      \"outputContent\": \"...\",\n" +
                "      \"isExample\": \"...\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
