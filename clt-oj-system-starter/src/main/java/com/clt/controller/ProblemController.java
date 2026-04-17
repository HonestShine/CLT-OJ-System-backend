package com.clt.controller;

import com.clt.dto.ProblemCreationOrUpdateDTO;
import com.clt.entity.Problem;
import com.clt.entity.Result;
import com.clt.enums.ProblemDifficulty;
import com.clt.utils.JwtUtil;
import com.clt.vo.ProblemAllInfoVO;
import com.clt.vo.ProblemRecommendVO;
import com.clt.vo.ProblemTitleInfoVO;
import com.clt.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取问题列表
     */
    @GetMapping("/problems/size")
    public Result getProblemList() {
        Integer problemListCount = problemService.getProblemListCount();
        return Result.success(problemListCount);
    }

    /**
     * 分页查询
     */
    @GetMapping("/problems/page")
    public Result getProblemList(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ProblemTitleInfoVO> problemList = problemService.getProblemListOfPage(start, pageSize);
        return Result.success(problemList);
    }

    /**
     * 根据题目ID获取题目详情
     */
    @GetMapping("/problems/{id}")
    public Result getProblemById(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return Result.error("无效的 ID");
        }

        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            return Result.error("404", "题目不存在");
        }
        ProblemAllInfoVO problemAllInfoVO = new ProblemAllInfoVO(
                problem.getId(),
                problem.getTitle(),
                problem.getDescription(),
                problem.getInputFormat(),
                problem.getOutputFormat(),
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                ProblemDifficulty.getMassage(problem.getDifficulty()),
                problem.getHint(),
                problem.getTags(),
                problem.getSamples()
        );

        return Result.success(problemAllInfoVO);
    }

    /**
     * 搜索问题(模糊匹配)
     */
    @GetMapping("/problems/search")
    public Result searchProblem(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        List<ProblemTitleInfoVO> problemList = problemService.searchProblem(keyword);
        return Result.success(problemList);
    }

    /**
     * 题目筛选(根据难度)
     */
    @GetMapping("/problems/filter")
    public Result filterProblem(@RequestParam(value = "difficulty", required = false, defaultValue = "") String difficulty,
                                @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ProblemTitleInfoVO> problemList = problemService.filterProblem(difficulty, start, pageSize);
        return Result.success(problemList);
    }

    /**
     * 新增问题
     */
    @PostMapping("/admin/problems")
    public Result createProblem(@RequestBody ProblemCreationOrUpdateDTO dto, @RequestHeader("token") String token) {
        // 验证用户身份
        if (jwtUtil.isUser(token)) {
            return Result.error("401", "用户身份验证失败，需管理员权限");
        }
        Problem problem = null;
        try {
            problem = problemService.CreateProblem(dto);
        } catch (Exception e) {
            return Result.error("500", "题目/题目描述/输入格式/输出格式不能为空 或 题目已存在");
        }

        if (problem == null) {
            return Result.error("500", "创建失败");
        }
        return Result.success();
    }

    /**
     * 修改问题
     */
    @PutMapping("/admin/problems")
    public Result updateProblem(@RequestBody ProblemCreationOrUpdateDTO dto, @RequestHeader("token") String token) {
        // 验证用户身份
        if (jwtUtil.isUser(token)) {
            return Result.error("401", "用户身份验证失败，需管理员权限");
        }
        if (dto.getId() == null || dto.getId() <= 0) {
            return Result.error("500", "题目ID为空");
        }
        Problem problem;
        try {
            problem = problemService.UpdateProblem(dto);
        } catch (Exception e) {
            return Result.error("500", "题目ID//输入格式/输出格式不能为空 或 题目不存在");
        }
        if (problem == null) {
            return Result.error("500", "修改失败");
        }
        return Result.success();
    }

    /**
     * 删除问题
     */
    @DeleteMapping("/admin/problems/{id}")
    public Result deleteProblem(@PathVariable Integer id, @RequestHeader("token") String token) {
        if (jwtUtil.isUser(token)) {
            return Result.error("401", "用户身份验证失败，需管理员权限");
        }
        if (id == null || id <= 0) {
            return Result.error("500", "题目ID为空");
        }
        boolean result;
        try {
            result = problemService.deleteProblem(id);
        } catch (Exception e) {
            return Result.error("500", "题目ID为空 或 题目不存在 或 程序异常");
        }
        if (!result) {
            return Result.error("500", "删除失败");
        }
        return Result.success();
    }

    /**
     * 获取推荐题目 3个
     */
    @GetMapping("/problems/recommend")
    public Result getRecommendProblems() {
        List<ProblemRecommendVO> problemList = problemService.getRecommendProblems();
        return Result.success(problemList);
    }

}
