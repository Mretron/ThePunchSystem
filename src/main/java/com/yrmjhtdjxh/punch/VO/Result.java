package com.yrmjhtdjxh.punch.VO;


import lombok.Data;

/**
 * @author dengg
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    private Result(T data) {
        this.code=200;
        this.msg="success";
        this.data = data;
    }

    private Result() {
        this.code=200;
        this.msg="success";
    }


    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> error(Integer code,String message){
        return new Result<>(code, message);
    }

    public static <T> Result success(T data) {
        return new Result<>(data);
    }

    public static Result success() {
        return new Result();
    }
}
