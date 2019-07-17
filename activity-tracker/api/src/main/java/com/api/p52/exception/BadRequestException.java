package com.api.p52.exception;

public class BadRequestException extends HttpException{
    public BadRequestException(){
        super("Bad request.",400);
    }
    public BadRequestException(String message){
        super(message,400);
    }
}
