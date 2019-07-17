package org.acrobatt.project.controllers;

import org.acrobatt.project.dto.LocationBody;
import org.acrobatt.project.model.mysql.Location;
import org.acrobatt.project.services.LocationService;
import org.acrobatt.project.utils.ResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/locations")
public class LocationController {

    private static Logger logger = LogManager.getLogger(LocationController.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllLocations() throws JSONException {
        try {
            List<Location> l = LocationService.findAllLocations();
            if(!l.isEmpty()) return ResponseUtils.createResponse(200, l);
            else             return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch(Exception e) {
            logger.error("Unreported exception occurred : ", e);
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/coord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getLocationByLatLong(@BeanParam LocationBody body) throws JSONException {
        try {
            Location l = LocationService.findLocationForCoordinates(body.latitude, body.longitude);
            if(l != null) return ResponseUtils.createResponse(200, l);
            else          return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch(Exception e) {
            logger.error("Unreported exception occurred : ", e);
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getLocationById(@PathParam("id") Long id) throws JSONException {
        if(id == null) return ResponseUtils.createResponse(400, "");
        try {
            Location l = LocationService.findLocationById(id);
            if(l != null)   return ResponseUtils.createResponse(200, l);
            else            return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error("Unreported exception occurred : ", e);
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response addNewLocation(LocationBody body) throws JSONException {
        if(body == null) return ResponseUtils.createResponse(400, "Paramètres invalides");
        try {
            Location l = LocationService.addNewLocation(body.cityName, body.latitude, body.longitude, null);
            if(l != null)   return ResponseUtils.createResponse(200, l);
            else            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"L'insertion a échoué\"}");
        } catch(Exception e) {
            logger.error("Unreported exception occurred : ", e);
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"L'insertion a échoué\"}");
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getCompactList() throws JSONException {
        try {
            List<String> locs = LocationService.getCompactNameList();
            logger.debug(locs.toString());
            return ResponseUtils.createResponse(200, locs);
        } catch(Exception e) {
            logger.error("Unreported exception occurred : ", e);
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }
}
