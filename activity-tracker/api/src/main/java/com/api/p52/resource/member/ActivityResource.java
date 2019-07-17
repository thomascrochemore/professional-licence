package com.api.p52.resource.member;

import com.api.p52.dao.ActivityDAO;
import com.api.p52.dao.PropertyDAO;
import com.api.p52.dao.SessionPropertyDAO;
import com.api.p52.exception.NotFoundException;
import com.api.p52.model.Activity;
import com.api.p52.model.ActivityRequest;
import com.api.p52.model.Property;
import com.api.p52.model.PropertyRequest;
import com.api.p52.validator.ActivityValidator;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/member/activity")
public class ActivityResource {
    @InjectParam
    ActivityDAO activityDAO;
    @InjectParam
    PropertyDAO propertyDAO;
    @InjectParam
    ActivityValidator activityValidator;

    /**
     * get all activities
     * , need authentication
     * @return all activities with id, name
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Activity> getAllActivities(){
        return activityDAO.findAll();
    }

    /**
     * get one activity of {activityId} id
     * , need authentication
     * @param activityId id of activity
     * @return activity with id, name
     */
    @GET
    @Path("/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Activity getOneActivity(@PathParam("activityId") Long activityId){
        Activity activity = activityDAO.findOne(activityId);
        if(activity == null){
            throw new NotFoundException("Activity not found.");
        }
        return activity;
    }

    /**
     * get all properties of activity with {activityId} id
     * , need authentication
     * @param activityId id of activity
     * @return properties with id, name, value_type
     */
    @GET
    @Path("/{activityId}/property")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Property> getPropertiesOfActivity(@PathParam("activityId") Long activityId){
        Activity activity = activityDAO.findOne(activityId);
        if(activity == null){
            throw new NotFoundException("Activity not found.");
        }
        return propertyDAO.findByActivity(activity);
    }

    /**
     * create an activity with properties
     * , need authentication
     * @param request activity with name, properties
     * @return created activity with id, name
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Activity createActivity(ActivityRequest request){
        activityValidator.activityValidator(request);
        Activity activity = new Activity(request.getName());
        List<Property> properties = new ArrayList<>();
        for(PropertyRequest propertyRequest : request.getPropertiesRequest()){
            properties.add(new Property(activity,propertyRequest.getName(),propertyRequest.getValue_type()));
        }
        activityDAO.insert(activity);
        propertyDAO.insertAll(properties);
        return activity;
    }
}
