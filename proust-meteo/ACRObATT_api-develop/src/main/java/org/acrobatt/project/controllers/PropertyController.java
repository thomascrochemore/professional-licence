package org.acrobatt.project.controllers;

import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.services.PropertyService;
import org.acrobatt.project.utils.ResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/properties")
public class PropertyController {

    private static Logger logger = LogManager.getLogger(PropertyController.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllProperties() throws JSONException {
        try {
            List<Property> p = PropertyService.findAllProperties();
            if(!p.isEmpty()) return ResponseUtils.createResponse(200, p);
            else             return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getPropertyById(@PathParam("id") Long id) throws JSONException {
        if(id == null) return ResponseUtils.createResponse(400, "");
        try {
            Property p = PropertyService.findPropertyById(id);
            if(p != null)   return ResponseUtils.createResponse(200, p);
            else            return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getCompactList() throws JSONException {
        try {
            List<String> props = PropertyService.getCompactNameList();
            logger.debug(props.toString());
            return ResponseUtils.createResponse(200, props);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }
}
