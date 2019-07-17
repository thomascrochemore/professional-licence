package com.api.p52.model;

public class SuccessMessage {
    private String message;
    private int status;

    public SuccessMessage(){
        this("Success.");
    }
    public SuccessMessage(String message){
        this(message,200);
    }
    public SuccessMessage(String message,int status){
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
