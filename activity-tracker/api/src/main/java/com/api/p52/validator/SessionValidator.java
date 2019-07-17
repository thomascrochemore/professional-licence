package com.api.p52.validator;

import com.api.p52.dao.ActivityDAO;
import com.api.p52.dao.PropertyDAO;
import com.api.p52.dao.SessionDAO;
import com.api.p52.dao.SessionPropertyDAO;
import com.api.p52.exception.BadRequestException;
import com.api.p52.model.*;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

import java.util.List;

@Singleton
public class SessionValidator {

    @InjectParam
    ActivityDAO activityDAO;

    @InjectParam
    SessionDAO sessionDAO;

    @InjectParam
    PropertyDAO propertyDAO;

    public void createSessionValidator(SessionRequest session){
        sessionValidator(session);
        arePropertiesInActivity(session);
    }
    public void updateSessionValidator(User currentUser,SessionRequest session){
        sessionValidator(session);
        isSameUser(currentUser,session);
    }
    private void sessionValidator(SessionRequest session){
        activityExistsValidator(session);
        dateNotNullValidator(session);
    }
    public void sessionPropertyValidator(SessionPropertyRequest sessionPropertyRequest,Property property){
        if(property.getValueType().equals("number") && sessionPropertyRequest.getValue_number() == null){
            throw new BadRequestException("Invalid property " + property.getName());
        }
        if(property.getValueType().equals("bool") && sessionPropertyRequest.getValue_bool() == null){
            throw new BadRequestException("Invalid property " + property.getName());
        }
        if(property.getValueType().equals("string") && sessionPropertyRequest.getValue_string() == null){
            throw new BadRequestException("Invalid property " + property.getName());
        }
    }
    private void activityExistsValidator(SessionRequest request){
        if(request.getActivityId() == null){
            throw new BadRequestException("Please fill out an activity for this session.");
        }
        if(activityDAO.findOne(request.getActivityId()) == null){
            throw new BadRequestException("Activity does not exists.");
        }
    }
    private void dateNotNullValidator(SessionRequest request){
        if(request.getDate() == null){
            throw new BadRequestException("Please fill out a date for this session.");
        }
    }
    private void isSameUser(User currentUser,SessionRequest request){
        Session session = sessionDAO.findOne(request.getId());
        if(!currentUser.getId().equals(session.getUserId())){
            throw new BadRequestException("You can update only yours sessions");
        }
    }
    private void arePropertiesInActivity(SessionRequest request){
        List<Property> properties = propertyDAO.findByActivityId(request.getActivityId());
        if(request.getProperties().size() != properties.size()){
            throw new BadRequestException("All properties of activity must be fill out");
        }
        for(Property property : properties){
            boolean propertyFound = false;
            for(SessionPropertyRequest prop : request.getProperties()){
                if(prop.getPropertyId().equals(property.getId())){
                    sessionPropertyValidator(prop,property);
                    propertyFound = true;
                }
            }
            if(!propertyFound){
                throw new BadRequestException("Please fill out the property " + property.getName());
            }
        }
    }

}
