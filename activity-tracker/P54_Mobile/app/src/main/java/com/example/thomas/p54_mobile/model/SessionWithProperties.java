package com.example.thomas.p54_mobile.model;

import java.util.ArrayList;
import java.util.List;

public class SessionWithProperties extends Session{
	
	private List<SessionProperty> properties;
	
	public SessionWithProperties() {
		setProperties(new ArrayList<SessionProperty>());
	}
	public SessionWithProperties(Session session) {
		this();
		setId(session.getId());
		setDate(session.getDate());
		setUserId(session.getUserId());
		setActivityId(session.getActivityId());
		setUser(session.getUser());
		setActivity(session.getActivity());
	}

	public List<SessionProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<SessionProperty> properties) {
		this.properties = properties;
	}


}
