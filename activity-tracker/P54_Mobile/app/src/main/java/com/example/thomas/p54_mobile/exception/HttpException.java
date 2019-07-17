package com.example.thomas.p54_mobile.exception;

import com.example.thomas.p54_mobile.model.ErrorMessage;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

public class HttpException extends Exception {
	
	private ErrorMessage errorMessage;

    public HttpException(){
        this("Internal error");
    }

    public HttpException(String message){
        this(message,500);
    }

    public HttpException(String message, int status){
    	this(new ErrorMessage(message,status));
    }
    public HttpException(ErrorMessage message) {
    	this.errorMessage = message;
    }
    
    public HttpException(HttpClientErrorException e) {
    	try {
	    	String body = e.getResponseBodyAsString();
	    	ObjectMapper mapper = new ObjectMapper();
	    	JsonNode node = mapper.readTree(body);
	    	String message = node.get("message").asText();
	    	this.errorMessage = new ErrorMessage(message,e.getStatusCode().value());
    	}catch(Exception exc) {
    		this.errorMessage = new ErrorMessage("Internal error.",500);
    	}
    }
    
    public String getError() {
    	return errorMessage.getMessage();
    }
    public int getStatus() {
    	return errorMessage.getStatus();
    }

}
