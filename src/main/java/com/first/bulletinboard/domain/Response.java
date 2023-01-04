package com.first.bulletinboard.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success(T result){
        return new Response("SUCCESS",result);
    }
    public static Response<Void> success(){
        return new Response("SUCCESS",null);
    }
    public static <T> Response<T> error(String resultCode, T result) {
        return new Response<>(resultCode, result);
    }
    //에러 리턴
    public static Response<ErrorResponse> error(ErrorResponse errorResponse) {
        return new Response("ERROR", errorResponse);
    }

}
