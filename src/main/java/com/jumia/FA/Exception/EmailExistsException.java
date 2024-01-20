package com.jumia.FA.Exception;

public class EmailExistsException extends Exception{

    private String message;

    public EmailExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
