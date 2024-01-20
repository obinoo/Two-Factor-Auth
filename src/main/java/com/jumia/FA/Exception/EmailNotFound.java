package com.jumia.FA.Exception;

public class EmailNotFound extends Exception{

    private String message;

    public EmailNotFound(String msg){
        super(msg);
        this.message = msg;
    }
}
