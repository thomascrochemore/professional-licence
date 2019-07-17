package com.api.p52.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table( name = "SESSION_PROPERTIES" )
public class SessionProperty
{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    private Float value_number = null;
    private Boolean value_bool = null;
    private String value_string = null;

    @JoinColumn(name="session_id",nullable=false)
    @ManyToOne
    private Session session;
    @JoinColumn(name="property_id",nullable=false)
    @ManyToOne
    private Property property;

    public SessionProperty() {	  }

    public SessionProperty(Session session, Property property, Float value_number)
    {
        this.session = session;
        this.property = property;
        this.value_number = value_number;
    }
    public SessionProperty(Session session, Property property, Boolean value_bool)
    {
        this.session = session;
        this.property = property;
        this.value_bool = value_bool;
    }
    public SessionProperty(Session session, Property property, String value_string)
    {
        this.session = session;
        this.property = property;
        this.value_string = value_string;
    }

    public Long getId()
    {
        return this.id;
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    public Float getValueNumber()
    {
        return this.value_number;
    }

    public void setValueNumber(Float value_number)
    {
        this.value_number = value_number;
    }

    public Boolean getValueBool()
    {
        return this.value_bool;
    }

    public void setValueBool(Boolean value_bool)
    {
        this.value_bool = value_bool;
    }

    public String getValueString()
    {
        return this.value_string;
    }

    public void setValueString(String value_string)
    {
        this.value_string = value_string;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}

