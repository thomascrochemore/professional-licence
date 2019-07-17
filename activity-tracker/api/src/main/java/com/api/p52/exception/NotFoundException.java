package com.api.p52.exception;

public class NotFoundException extends HttpException {

    public NotFoundException(){
        super("Page not found.",404);
    }
    public NotFoundException(String message){
        super(message,404);
    }
}
