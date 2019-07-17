package com.example.thomas.p54_mobile.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by thomas on 12/01/2018.
 */

public class Property
{
    private Long id;
    private String name;
    private String value_type;
    private Long activityId;
    @JsonIgnore
    private Activity activity;

    public Property() {	  }

    public Property(Activity activity, String name, String value_type) {
        this.activity = activity;
        this.name = name;
        this.value_type = value_type;
    }

    public Long getId()
    {
        return this.id;
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValueType()
    {
        return this.value_type;
    }

    public void setValueType(String value_type) {
        this.value_type = value_type;
    }

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
}

