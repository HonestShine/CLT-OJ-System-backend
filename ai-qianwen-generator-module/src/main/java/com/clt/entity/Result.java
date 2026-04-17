package com.clt.entity;

import lombok.Data;

@Data
public class Result {

    private String code;    //状态码
    private String message;     //返回信息
    private Object data;    //返回数据

    public static Result success() {
        Result result = new Result();
        result.code = "200";
        result.message = "success";
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.data = data;
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.code = "400";
        result.message = msg;
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.code = code;
        result.message = msg;
        return result;
    }

}
