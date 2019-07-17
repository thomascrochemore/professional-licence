package com.api.p52.properties;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class YamlPropertyParser {
	
	public YamlPropertyParser() {
		
	}
	
	public Properties parse(Iterable<String> files) throws FileNotFoundException, YamlException {
		Map<String,String> properties = new HashMap<String,String>();
		for(String path : files) {
			InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
			Reader targetReader = new InputStreamReader(stream);
			YamlReader reader = new YamlReader(targetReader);
			Object obj = reader.read();
			if(obj instanceof Map) {
				setProperties("",properties,(Map) obj);
			}
		}
		return new Properties(properties);
	}
	private void setProperties(String prefix,Map<String,String> toOverride,Map<String,Object> toParse) {
		
		for(Map.Entry<String, Object> entry : toParse.entrySet()) {
			String propName = prefix + entry.getKey();
			if(entry.getValue() instanceof String) {
				toOverride.put(propName,(String) entry.getValue());
			}else if(entry.getValue() instanceof Map) {
				setProperties(propName+".",toOverride,(Map) entry.getValue());
			}
			
		}
	}

}
