package com.jumia.FA.Exception;

public class ExpiredJwtException {
    private String message;

    public ExpiredJwtException(String msg){
        this.message = msg;
    }
}
