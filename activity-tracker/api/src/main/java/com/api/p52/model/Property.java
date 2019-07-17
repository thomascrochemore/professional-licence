package com.api.p52.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table( name = "PROPERTIES" )
public class Property
{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    private String name;
    private String value_type;

    @JoinColumn(name="activity_id",nullable=false)
    @ManyToOne
    @JsonIgnore
    private Activity activity;

    public Property() {	  }

    public Property(Activity activity, String name, String value_type)
    {
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

    public void setValueType(String value_type)
    {
        this.value_type = value_type;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getActivityId(){
        return activity == null ? null : activity.getId();
    }
}
