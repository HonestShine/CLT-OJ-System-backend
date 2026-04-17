package com.clt.controller;

import com.clt.dto.SubmissionCodeDTO;
import com.clt.entity.JudgeResult;
import com.clt.entity.Result;
import com.clt.service.SubmissionService;
import com.clt.utils.JwtUtil;
import com.clt.vo.SubmissionResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/submissions")
@RestController
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 提交代码
     */
    @PostMapping
    public Result submitCode(@RequestBody SubmissionCodeDTO submissionCodeDTO, @RequestHeader("token") String token) throws Exception {
        //校验题目 ID 是否有效
        if (submissionCodeDTO.getProblemId() <= 0) {
            return Result.error("无效的题目 ID");
        }
        //确保代码不为空（虽然@NotBlank 已经校验）
        if (submissionCodeDTO.getCode() == null || submissionCodeDTO.getCode().trim().isEmpty()) {
            return Result.error("提交的代码不能为空");
        }
        //利用token获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);

        SubmissionResultVO result =  submissionService.submitCode(submissionCodeDTO, userId);

        if (result == null) {
            return Result.error("提交失败");
        }

        return Result.success(result);
    }

    /**
     * 通过题目ID获取提交记录列表
     */
    @GetMapping("/problem/{problemId}")
    public Result getSubmissionList(@PathVariable Integer problemId, @RequestHeader("token") String token) {
        //排除ID为空
        if (problemId == null) {
            return Result.error("404", "题目不存在");
        }
        //利用token获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        //依据题目ID与用户ID查询提交记录，返回结果
        List<SubmissionResultVO> submissionList = submissionService.getSubmissionResult(problemId, userId);
        if (submissionList == null || submissionList.isEmpty()) {
            return Result.error("404", "提交记录不存在");
        }
        return Result.success(submissionList);
    }


    /**
     * 获取题目的提交结果详细信息
     */
    @GetMapping("/{id}")
    public Result getSubmissionResult(@PathVariable Integer id) {
        //排除ID为空
        if (id == null) {
            return Result.error("无效的提交 ID");
        }
        //依据ID查询数据表，返回结果
        JudgeResult result = submissionService.getSubmissionResult(id);

        if (result == null) {
            return Result.error("提交结果不存在");
        }

        return Result.success(result);
    }

    /**
     * 获取所有提交结果
     */
    @GetMapping
    public Result getAllSubmissionResult() {
        return Result.success(submissionService.getAllSubmissionResult());
    }

    /**
     * 获取用户提交结果
     */
    @GetMapping("/user/{userId}")
    public Result getUserSubmissionResult(@PathVariable Integer userId) {
        return Result.success(submissionService.getUserSubmissionResult(userId));
    }

}
