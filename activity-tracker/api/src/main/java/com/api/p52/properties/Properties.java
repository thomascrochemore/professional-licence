package com.api.p52.properties;

import java.util.Map;

public class Properties {
	private Map<String,String> map;
	
	public Properties(Map<String,String> map) {
		this.map = map;
	}
	protected void setProperty(String name,String value) {
		map.put(name, value);
	}
	public String getProperty(String varName) {
		return map.get(varName);
	}
	public Boolean getBooleanProperty(String varName){
		String value = getProperty(varName);
		if(value == null){
			return null;
		}
		return value.toLowerCase().equals("true") ? true : false;
	}
	public Integer getIntegerProperty(String varName){
		try{
			return Integer.valueOf(getProperty(varName));
		}catch (Throwable e){
			return null;
		}
	}
	public boolean hasProperty(String varName) {
		return map.containsKey(varName);
	}

}
