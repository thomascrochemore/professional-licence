package com.example.thomas.p54_mobile.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithProperties extends Activity{
	private List<Property> properties;
	
	public ActivityWithProperties() {
		setProperties(new ArrayList<Property>());
	};
	
	public ActivityWithProperties(Activity activity) {
		this();
		setId(activity.getId());
		setName(activity.getName());
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

}
