package com.clt.utils;

import okhttp3.*;
import org.json.JSONObject;

public class Judge0Util {
    // Judge0 官方公共接口
    private static final String BASE_URL = "https://ce.judge0.com";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    // Judge0 公共 API 的限制
    private static final double MAX_CPU_TIME_LIMIT = 20.0;
    private static final int MAX_MEMORY_LIMIT = 2048000;

    /**
     * 运行代码
     * @param languageId 语言ID
     * @param sourceCode 源代码
     * @param stdin 输入
     * @param timeLimit 时间限制(s)，最大20.0s
     * @param memoryLimit 内存限制(KB)，最大2048000KB
     * @return 运行结果
     */
    public static JSONObject runCode(int languageId, String sourceCode, String stdin, Double timeLimit, Integer memoryLimit) throws Exception {
        // 限制时间和内存不超过 Judge0 公共 API 的最大值
        if (timeLimit == null || timeLimit > MAX_CPU_TIME_LIMIT) {
            timeLimit = MAX_CPU_TIME_LIMIT;
        }
        if (memoryLimit == null || memoryLimit > MAX_MEMORY_LIMIT) {
            memoryLimit = MAX_MEMORY_LIMIT;
        }

        // 构造请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("language_id", languageId);
        requestBody.put("source_code", sourceCode);
        requestBody.put("stdin", stdin);
        requestBody.put("cpu_time_limit", timeLimit);
        requestBody.put("memory_limit", memoryLimit);

        // 构造请求
        Request request = new Request.Builder()
                .url(BASE_URL + "/submissions?wait=true")
                .post(RequestBody.create(
                        MediaType.parse("application/json"),
                        requestBody.toString()
                ))
                .build();

        // 发送请求
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求失败：" + response.code() + ", " + response.body().string());
            }
            return new JSONObject(response.body().string());
        }
    }
}