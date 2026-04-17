package com.clt.enums;

public enum ProblemDifficulty {
    EASY(1, "简单"),   //简单
    MEDIUM(2, "中等"), //中等
    HARD(3, "困难");    //困难

    public final int code;
    public final String massage;

    ProblemDifficulty(int code, String massage) {
        this.code = code;
        this.massage = massage;
    }

    public static int getCode(ProblemDifficulty problemDifficulty) {
        return problemDifficulty.code;
    }

    public static String getMassage(int code) {
        for (ProblemDifficulty problemDifficulty : ProblemDifficulty.values()) {
            if (problemDifficulty.code == code) {
                return problemDifficulty.massage;
            }
        }
        return null;
    }

    public static int getCode(String massage) {
        for (ProblemDifficulty problemDifficulty : ProblemDifficulty.values()) {
            if (problemDifficulty.massage.equals(massage)) {
                return problemDifficulty.code;
            }
        }
        return -1;
    }
}
