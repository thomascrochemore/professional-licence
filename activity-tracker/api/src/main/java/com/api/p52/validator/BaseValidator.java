package com.api.p52.validator;

import com.api.p52.exception.BadRequestException;

public class BaseValidator {
    protected void stringNotBlankMinMaxFieldValidator(String field,String value,int min,int max){
        notNullFieldValidator(field,value);
        notEmptyFieldValidator(field,value);
        minLengthFieldValidator(field,value,min);
        maxLengthFieldValidator(field,value,max);
    }
    protected void notNullFieldValidator(String field,Object value){
        notNullValidator(value,"Please fill out the field " + field + ".");
    }
    protected void notNullValidator(Object value,String message){
        if(value == null){
            throw new BadRequestException(message);
        }
    }
    protected void notEmptyFieldValidator(String field,String value){
        notEmptyValidator(value,"Please fill out the field " + field + ".");
    }
    protected void notEmptyValidator(String value,String message){
        if(value.isEmpty()){
            throw new BadRequestException(message);
        }
    }
    protected void minLengthFieldValidator(String field,String value,int min){
       minLengthValidator(value,min,"The field " + field + " must have at least " + min +" characters.");
    }
    protected void minLengthValidator(String value,int min,String message){
        if(value.length() < min){
            throw new BadRequestException(message);
        }
    }
    protected void maxLengthFieldValidator(String field,String value,int max){
        maxLengthValidator(value,max,"The field "+ field + " must have " + max + "characters at the most.");
    }
    protected void maxLengthValidator(String value,int max,String message) {
        if (value.length() > max) {
            throw new BadRequestException(message);
        }
    }
    protected void matchFieldValidator(String field,String value,String expected){
        matchValidator(value,expected,"The field " + field + " has incorrect characters.");
    }
    protected void matchValidator(String value,String expected,String message){
        if(!value.matches(expected)){
            throw new BadRequestException(message);
        }
    }

}
