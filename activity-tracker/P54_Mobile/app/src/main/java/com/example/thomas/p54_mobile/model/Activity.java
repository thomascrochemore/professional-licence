package com.example.thomas.p54_mobile.model;

public class Activity
{
    private Long id;
    private String name;

    public Activity() {	  }

    public Activity(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return this.id;
    }

    protected void setId(Long id)
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
}
