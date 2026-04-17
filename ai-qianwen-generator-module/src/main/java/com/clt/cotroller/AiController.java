package com.clt.cotroller;

import com.clt.entity.Result;
import com.clt.service.AiQuestionService;
import com.clt.utils.JwtUtil;
import com.clt.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI 题目生成控制器
 * 提供 /ai POST 接口用于根据提示词生成题目。
 */
@RestController
@RequestMapping("/admin/ai")
public class AiController {

    @Autowired
    private AiQuestionService aiQuestionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Get /ai
     * 根据前端提供的提示词生成题目。
     * @param promptWord 用户输入的提示词
     * @return QuestionResponse 包含生成的题目信息的 JSON 对象
     */
    @GetMapping
    public Result generateQuestion(@RequestParam("promptWord") String promptWord, @RequestHeader("token") String token) {
        // 验证用户身份是否是管理员
        if (jwtUtil.isUser(token)) {
            return Result.error("401", "用户身份验证失败，需管理员权限");
        }
        // 调用服务层逻辑生成题目
        QuestionVO questionVO = aiQuestionService.generateQuestion(promptWord);
        if (questionVO.getTitle().equals("生成失败")) {
            return Result.error("500", questionVO.getDescription());
        }
        return Result.success(questionVO);
    }
}
