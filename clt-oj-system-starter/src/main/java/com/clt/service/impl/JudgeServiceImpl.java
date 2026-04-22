package com.clt.service.impl;

import com.clt.dto.SubmissionCodeDTO;
import com.clt.entity.JudgeResult;
import com.clt.entity.Problem;
import com.clt.entity.ProblemSample;
import com.clt.enums.Languages;
import com.clt.enums.SubmissionStatus;
import com.clt.mapper.ProblemMapper;
import com.clt.mapper.ProblemSampleMapper;
import com.clt.service.JudgeService;
import com.clt.utils.Judge0Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemSampleMapper problemSampleMapper;

    // 判题
    @Override
    public JudgeResult judge(SubmissionCodeDTO submissionCodeDTO) throws RuntimeException {
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setSubmitTime(LocalDateTime.now());

        // 1. 获取题目信息
        Problem problem = problemMapper.getProblemById(submissionCodeDTO.getProblemId());
        if (problem == null) {
            judgeResult.setStatus(SubmissionStatus.INTERNAL_ERROR.message);
            judgeResult.setMessage("题目不存在");
            return judgeResult;
        }

        // 2. 获取样例列表
        List<ProblemSample> problemSampleList = problemSampleMapper.getProblemSampleListByProblemId(submissionCodeDTO.getProblemId());
        if (problemSampleList == null || problemSampleList.isEmpty()) {
            judgeResult.setStatus(SubmissionStatus.INTERNAL_ERROR.message);
            judgeResult.setMessage("题目样例为空");
            return judgeResult;
        }

        // 3. 初始化判题结果
        String finalStatus = SubmissionStatus.ACCEPTED.message;
        BigDecimal maxRuntime = BigDecimal.ZERO;
        BigDecimal maxMemory = BigDecimal.ZERO;
        String stdout = "";
        String stderr = "";
        String compileOutput = "";
        String message = "";

        // 4. 遍历样例进行评测
        for (ProblemSample problemSample : problemSampleList) {
            try {
                // 调用 Judge0
                int languageCode = getLanguageCode(submissionCodeDTO.getLanguage());
                if (languageCode == -1) {
                    finalStatus = SubmissionStatus.COMPILE_ERROR.message;
                    compileOutput = "不支持的编程语言: " + submissionCodeDTO.getLanguage();
                    break;
                }

                // 注意：根据 Judge0 文档，内存和时间限制通常需要根据具体 API 要求传参
                // 这里假设 Judge0Util.runCode 处理了单位转换，或者 API 接受秒和 KB
                JSONObject result = Judge0Util.runCode(
                        languageCode,
                        submissionCodeDTO.getCode(),
                        problemSample.getInputContent(),
                        (double) problem.getTimeLimit().intValue(), // 单位是秒
                        problem.getMemoryLimit().intValue() * 1024 // 单位是 KB
                );

                // 5. 解析 Judge0 返回结果 (参考文档字段)
                // status.id: 1-14, status.description: "In Queue", "Accepted" 等
                JSONObject statusObj = result.getJSONObject("status");
                int statusCodeId = statusObj.getInt("id"); // 利用 ID 进行精准判断
                String statusDescription = statusObj.getString("description");

                String stdoutOfTestCase = result.optString("stdout", null);
                String stderrOfTestCase = result.optString("stderr", null);
                String compileOutputOfTestCase = result.optString("compile_output", null);
                // 注意：文档中 time 单位是 s, memory 单位是 KB
                BigDecimal runtimeOfTestCase = new BigDecimal(result.optString("time", "0"));
                BigDecimal memoryOfTestCase = new BigDecimal(result.optString("memory", "0"));

                // 6. 核心判题逻辑：根据 Judge0 的 Status ID (最权威) 进行判断
                // 参考文档：ID 3 是 Accepted，其他均为错误或异常
                if (statusCodeId != 3) { // 只要不是 Accepted (3)
                    // 直接映射 Judge0 的状态到你的系统
                    finalStatus = mapJudge0StatusById(statusCodeId, statusDescription);

                    // 记录该测试用例的详细错误信息
                    stdout = stdoutOfTestCase;
                    stderr = stderrOfTestCase;
                    compileOutput = compileOutputOfTestCase;
                    message = result.optString("message", ""); // 系统内部消息

                    // 关键逻辑：一旦出现非 AC 状态，立即跳出，不再评测后续样例
                    break;
                }

                // 7. Judge0 状态为 Accepted (ID=3)，检查答案是否正确 (比对 stdout)
                // 注意：Judge0 的 Accepted 仅代表程序正常退出，不代表输出符合题目预期
                if (stdoutOfTestCase == null) stdoutOfTestCase = "";
                if (problemSample.getOutputContent() == null) problemSample.setOutputContent("");

                // 简单的标准化比对（去除首尾空白和换行符差异）
                if (!stdoutOfTestCase.trim().equals(problemSample.getOutputContent().trim())) {
                    finalStatus = SubmissionStatus.WRONG_ANSWER.message;
                    stdout = stdoutOfTestCase; // 展示用户程序的输出
                    // 注意：这里也 Break，因为需求是“一旦非 AC 就返回”
                    break;
                }

                // 8. 如果通过了上述所有检查 (AC)，更新最大时间和内存
                if (runtimeOfTestCase.compareTo(maxRuntime) > 0) {
                    maxRuntime = runtimeOfTestCase;
                }
                if (memoryOfTestCase.compareTo(maxMemory) > 0) {
                    maxMemory = memoryOfTestCase;
                }

                // 如果是 AC，继续下一个样例
            } catch (Exception e) {
                finalStatus = SubmissionStatus.INTERNAL_ERROR.message;
                message = "系统异常: " + e.getMessage();
                break; // 系统错误，停止评测
            }
        }

        // 9. 填充最终结果
        // 只有当最终状态是 AC 时，maxRuntime 和 maxMemory 才是有意义的
        judgeResult.setStatus(finalStatus);
        judgeResult.setProblemId(problem.getId());
        judgeResult.setLanguage(submissionCodeDTO.getLanguage());
        judgeResult.setCode(submissionCodeDTO.getCode());
        judgeResult.setStdout(stdout);
        judgeResult.setStderr(stderr);
        judgeResult.setCompileOutput(compileOutput);
        judgeResult.setMessage(message);

        // 如果是 AC，设置性能数据；否则设为 0 或 null
        if (SubmissionStatus.ACCEPTED.message.equals(finalStatus)) {
            judgeResult.setRuntime(maxRuntime.doubleValue());
            judgeResult.setMemory(maxMemory.doubleValue());
        } else {
            judgeResult.setRuntime(0.0);
            judgeResult.setMemory(0.0);
        }

        return judgeResult;
    }

    /**
     * 根据 Judge0 的 Status ID 映射到本地枚举
     * 参考文档：Judge0参数详解.md
     */
    private String mapJudge0StatusById(int id, String description) {
        return switch (id) {
            case 3 -> SubmissionStatus.ACCEPTED.message; // 正常情况，理论上不会在这里被调用
            case 4 -> SubmissionStatus.WRONG_ANSWER.message;
            case 5 -> SubmissionStatus.TIME_LIMIT_EXCEEDED.message;
            case 6 -> SubmissionStatus.COMPILE_ERROR.message;
            case 7 -> SubmissionStatus.RUNTIME_ERROR_SIGSEGV.message;
            case 8 -> SubmissionStatus.RUNTIME_ERROR_SIGXFSZ.message;
            case 9 -> SubmissionStatus.RUNTIME_ERROR_SIGFPE.message;
            case 10 -> SubmissionStatus.RUNTIME_ERROR_SIGABRT.message;
            case 11 -> SubmissionStatus.RUNTIME_ERROR_NZEC.message;
            case 13 -> SubmissionStatus.INTERNAL_ERROR.message;
            case 14 -> SubmissionStatus.EXEC_FORMAT_ERROR.message;
            default -> {
                System.err.println("未知的 Judge0 状态 ID: " + id + ", 描述: " + description);
                yield SubmissionStatus.UNKNOWN_ERROR.message;
            }
        };
    }

    // 语言映射保持不变，但建议根据文档中的 ID 进行核对
    private int getLanguageCode(String language) {
        return switch (language) {
            case "C", "c", "C(GCC 14.1.0)" -> Languages.C.code; // Judge0 C 常用 9, 48, 50
            case "C++", "cpp", "C++(GCC 14.1.0)" -> Languages.CPP.code; // Judge0 C++ 常用 105, 54
            case "Java", "java", "Java(OpenJDK 17.0.6)" -> Languages.JAVA_17.code; // Judge0 Java 常用 91, 62
            case "Python", "python", "Python(3.13.2)" -> Languages.PYTHON_3.code; // Judge0 Python 常用 109, 92, 71
            case "Go", "go", "Go(1.22.0)" -> Languages.GO.code; // Judge0 Go 常用 106, 95
            // ... 其他语言映射
            default -> {
                System.err.println("未识别的编程语言：" + language);
                yield -1;
            }
        };
    }
}