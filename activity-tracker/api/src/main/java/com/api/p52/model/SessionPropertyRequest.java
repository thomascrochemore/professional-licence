package com.api.p52.model;

public class SessionPropertyRequest {
    private Float value_number = null;
    private Boolean value_bool = null;
    private String value_string = null;

    private Long propertyId;

    public Float getValue_number() {
        return value_number;
    }

    public void setValue_number(Float value_number) {
        this.value_number = value_number;
    }

    public Boolean getValue_bool() {
        return value_bool;
    }

    public void setValue_bool(Boolean value_bool) {
        this.value_bool = value_bool;
    }

    public String getValue_string() {
        return value_string;
    }

    public void setValue_string(String value_string) {
        this.value_string = value_string;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}
