package com.example.thomas.p54_mobile.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.ActivityRequest;
import com.example.thomas.p54_mobile.model.ActivityWithProperties;
import com.example.thomas.p54_mobile.model.Property;

/**
 * Created by thomas on 09/01/2018.
 */

public class ActivityService
{
    public static Activity createActivity(ActivityRequest createActivity) throws HttpException
    {
        return RestService.post("/member/activity",createActivity,ActivityRequest.class);
    }
    
    public static Activity findOne(Long activityId) throws HttpException
    {
    	return RestService.get("/member/activity/"+activityId,Activity.class);
    }

    public ActivityWithProperties findOneWithProperties(Long activityId) throws HttpException
    {
    	ActivityWithProperties activity = new ActivityWithProperties(findOne(activityId));
    	activity.setProperties(findPropertiesOfActivity(activityId));
    	return activity;
    }

    public static List<Property> findPropertiesOfActivity(Long activityId) throws HttpException
    {
    	return new ArrayList(Arrays.asList(RestService.get("/member/activity/"+activityId+"/property",Property[].class)));
    }
}
