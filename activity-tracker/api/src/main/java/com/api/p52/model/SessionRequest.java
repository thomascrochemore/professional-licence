package com.api.p52.model;

import java.util.ArrayList;
import java.util.List;

public class SessionRequest extends Session {
    private Long activityId;
    private List<SessionPropertyRequest> properties;

    public SessionRequest(){
        properties = new ArrayList<>();
    }


    @Override
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<SessionPropertyRequest> getProperties() {
        return properties;
    }

    public void setProperties(List<SessionPropertyRequest> properties) {
        this.properties = properties;
    }
}
