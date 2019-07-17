package com.api.p52.properties;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
public class ApplicationProperties {
	
	public final String CONFIG_PATH = "env/env.yaml";
	
	private Properties props;
	
	public ApplicationProperties() throws FileNotFoundException, YamlException, URISyntaxException {
		props = loadEnvironment();
	}
	
	public String getProperty(String name) {
		return props.getProperty(name);
	}
	public boolean hasProperty(String name) {
		return props.hasProperty(name);
	}
	public Boolean getBooleanProperty(String name){
		return props.getBooleanProperty(name);
	}
	public Integer getIntegerProperty(String varName){
		return props.getIntegerProperty(varName);
	}
	private Properties loadEnvironment() throws FileNotFoundException, YamlException, URISyntaxException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_PATH);
		Reader targetReader = new InputStreamReader(stream);
		YamlReader reader = new YamlReader(targetReader);
		Map map = (Map) reader.read();
		String env = map.get("env").toString();
		Map<String,Object> profiles = (Map) map.get("profiles");
		List<String> files = (List) profiles.get(env);
		YamlPropertyParser parser = new YamlPropertyParser();
		Properties properties = parser.parse(files);
		properties.setProperty("env",env);
		return properties;
	}


}
