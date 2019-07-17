package com.example.thomas.p54_mobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 12/01/2018.
 */

public class SessionRequest extends Session
{
    private Long activityId;
    private List<SessionPropertyRequest> properties;

    public SessionRequest(){
        properties = new ArrayList<>();
    }

    public Long getActivityId() {
        return activityId;
    }

    public List<SessionPropertyRequest> getProperties() {
        return properties;
    }

    public void setProperties(List<SessionPropertyRequest> properties) {
        this.properties = properties;
    }
}

