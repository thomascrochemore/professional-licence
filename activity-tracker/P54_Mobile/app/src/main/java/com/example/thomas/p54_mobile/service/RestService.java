package com.example.thomas.p54_mobile.service;

import com.example.thomas.p54_mobile.exception.HttpException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RestService
{
	private static RestTemplate restTemplate = new RestTemplate();
	private static String baseurl = "";
	private static HttpHeaders headers = new HttpHeaders();
	
	static
	{
		// configure your ObjectMapper here
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter ());
		RestTemplate restTemplate = new RestTemplate();    
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
	
	public static void setBasurl(String baseurl) {
		RestService.baseurl = baseurl;
	}
	public static String getBaseurl()
	{
		return RestService.baseurl;
	}

	public static void setHeader(String name,String value)
	{
		unsetHeader(name);
		headers.add(name, value);
	}

	public static void unsetHeader(String name) {
		headers.remove(name);
	}

	public static <T> T get(String path,Class<T> type,Object... uriVariables) throws HttpException
	{
		return exchange(path,HttpMethod.GET,null,type,uriVariables);
	}

	public static <R,T> T post(String path,R request,Class<T> type,Object... uriVariables) throws HttpException
	{
		return exchange(path,HttpMethod.POST,request,type,uriVariables);
	}

	public static <R,T> T put(String path,R request,Class<T> type,Object... uriVariables) throws HttpException
	{
		return exchange(path,HttpMethod.PUT,request,type,uriVariables);
	}
	
	public static <R,T> T delete(String path,Class<T> type,Object... uriVariables) throws HttpException
	{
		return exchange(path,HttpMethod.DELETE,null,type,uriVariables);
	}
	
	public static <R,T> T exchange(String path,HttpMethod method,R request,Class<T> type,Object... uriVariables) throws HttpException
	{
		try
		{
			HttpEntity req = null;
			if(request == null)
			{
				req = new HttpEntity<Void>(headers);
			}
			else
			{
				req = new HttpEntity<R>(request,headers);
			}
			ResponseEntity<T> response = restTemplate.exchange(baseurl+path,method, req, type, uriVariables);
			return response.getBody();
		}
		catch(HttpClientErrorException e)
		{
			throw new HttpException(e);
		}
	}
	
}
