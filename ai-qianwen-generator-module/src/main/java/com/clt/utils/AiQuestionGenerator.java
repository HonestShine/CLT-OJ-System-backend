package com.clt.utils;

import com.clt.service.AiQuestionService;
import com.clt.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AI 题目生成工具类
 * 供主业务模块直接调用，封装了与 AI 服务的交互逻辑。
 * 该类被 Spring 管理，需要被主业务模块注入。
 */
@Component
public class AiQuestionGenerator {

    @Autowired
    private AiQuestionService aiQuestionService;

    /**
     * 根据提示词生成题目
     * @param promptWord 提示词
     * @return QuestionResponse 生成的题目数据
     */
    public QuestionVO generateQuestion(String promptWord) {
        return aiQuestionService.generateQuestion(promptWord);
    }
}
