package com.api.p52.model;

public class ErrorMessage {
    private String message;
    private int status;

    public ErrorMessage(){
        this("Internal error",500);
    }
    public ErrorMessage(String message,int status){
        setMessage(message);
        setStatus(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
