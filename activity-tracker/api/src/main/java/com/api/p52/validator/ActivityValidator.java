package com.api.p52.validator;

import com.api.p52.dao.ActivityDAO;
import com.api.p52.exception.BadRequestException;
import com.api.p52.model.ActivityRequest;
import com.api.p52.model.PropertyRequest;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

import java.util.Arrays;
import java.util.List;

@Singleton
public class ActivityValidator extends BaseValidator{

    @InjectParam
    ActivityDAO activityDAO;

    public void activityValidator(ActivityRequest request){
        notNullFieldValidator("properties",request.getPropertiesRequest());
        stringNotBlankMinMaxFieldValidator("name",request.getName(),3,20);
        activityUniqueNameValidator(request);
        activityPropertiesValidator(request);
    }

    private void activityUniqueNameValidator(ActivityRequest request){
        if(activityDAO.findOneByName(request.getName()) != null){
            throw new BadRequestException("Activity name already exists");
        }
    }
    private void  activityPropertiesValidator(ActivityRequest request){
        List<String> types = Arrays.asList(new String[]{"bool","number","string"});
        for(PropertyRequest propertyRequest : request.getPropertiesRequest()){
            stringNotBlankMinMaxFieldValidator("property name",propertyRequest.getName(),3,20);
            notNullFieldValidator("property type",propertyRequest.getValue_type());
            if(!types.contains(propertyRequest.getValue_type())){
                throw new BadRequestException("Invalid property type");
            }
        }
    }
}
