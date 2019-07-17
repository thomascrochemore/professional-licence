package com.api.p52.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class ActivityRequest extends Activity {

    @JsonProperty("properties")
    private List<PropertyRequest> propertiesRequest;

    public List<PropertyRequest> getPropertiesRequest() {
        return propertiesRequest;
    }

    public void setPropertiesRequest(List<PropertyRequest> propertiesRequest) {
        this.propertiesRequest = propertiesRequest;
    }
}
