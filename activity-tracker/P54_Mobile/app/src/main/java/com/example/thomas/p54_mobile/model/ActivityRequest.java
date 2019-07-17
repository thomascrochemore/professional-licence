package com.example.thomas.p54_mobile.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by thomas on 12/01/2018.
 */

public class ActivityRequest extends Activity
{
    @JsonProperty("properties")
    private List<PropertyRequest> propertiesRequest;

    public List<PropertyRequest> getPropertiesRequest() {
        return propertiesRequest;
    }

    public void setPropertiesRequest(List<PropertyRequest> propertiesRequest) {
        this.propertiesRequest = propertiesRequest;
    }
}
