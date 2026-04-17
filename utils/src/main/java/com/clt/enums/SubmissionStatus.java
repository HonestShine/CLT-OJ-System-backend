package com.clt.enums;

public enum SubmissionStatus {
    IN_QUEUE(1, "In Queue"),    //排队中 --> IQ
    PROCESSING(2, "Processing"),  //执行中 --> PR
    ACCEPTED(3, "Accepted"),    //通过 --> AC
    WRONG_ANSWER(4, "Wrong Answer"),    //错误 --> WA
    TIME_LIMIT_EXCEEDED(5, "Time Limit Exceeded"),  //超时 --> TLE
    COMPILE_ERROR(6, "Compilation Error"),   //编译错误 --> CE
    RUNTIME_ERROR_SIGSEGV(7, "Runtime Error (SIGSEGV)"),   //运行时错误(段错误/数组越界) --> RE_SIGSEGV
    RUNTIME_ERROR_SIGXFSZ(8, "Runtime Error (SIGXFSZ)"),   //运行时错误(文件大小超限) --> RE_SIGXFSZ
    RUNTIME_ERROR_SIGFPE(9, "Runtime Error (SIGFPE)"),   //运行时错误(浮点异常/除零) --> RE_SIGFPE
    RUNTIME_ERROR_SIGABRT(10, "Runtime Error (SIGABRT)"),   //运行时错误(程序异常终止) --> RE_SIGABRT
    RUNTIME_ERROR_NZEC(11, "Runtime Error (NZEC)"),   //运行时错误(非零退出码) --> RE_NZEC
    RUNTIME_ERROR_OTHER(12, "Runtime Error (Other)"),   //运行时错误(其他错误) --> RE_OTHER
    INTERNAL_ERROR(13, "Internal Error"),  //系统内部错误 --> IE
    EXEC_FORMAT_ERROR(14, "Exec Format Error"),  //可执行文件格式错误 --> EFE
    UNKNOWN_ERROR(15, "Unknown Error"), //未知错误 --> UE
    MEMORY_OUT_OF_RANGE(17, "Memory Out Of Range"); //内存超限 --> MOR

    public final int code;
    public final String message;

    SubmissionStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static int getCode(SubmissionStatus submissionStatus) {
        return submissionStatus.code;
    }

    public static String getMessage(SubmissionStatus submissionStatus) {
        return submissionStatus.message;
    }

    public static SubmissionStatus getMessageByCode(int code) {
        for (SubmissionStatus message : SubmissionStatus.values()) {
            if (message.code == code) {
                return message;
            }
        }
        return null;
    }

    public static SubmissionStatus getCodeByMessage(String message) {
        for (SubmissionStatus code : SubmissionStatus.values()) {
            if (code.message.equals(message)) {
                return code;
            }
        }
        return null;
    }
}
