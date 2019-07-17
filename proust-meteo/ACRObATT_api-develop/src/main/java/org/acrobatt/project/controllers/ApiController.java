package org.acrobatt.project.controllers;

import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.services.ApiService;
import org.acrobatt.project.utils.ResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/apis")
public class ApiController {

    private static Logger logger = LogManager.getLogger(WeatherDataController.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllApis() throws JSONException {
        try {
            List<Api> a = ApiService.findAllApis();
            logger.debug("apiSize = [" + a.size() + "]");
            if(!a.isEmpty()) return ResponseUtils.createResponse(200, a);
            else             return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/withcfg")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllApisWithConfig() throws JSONException {
        try {
            List<Api> a = ApiService.findAllApisWithConfig();
            logger.debug("apiSize = [" + a.size() + "]");
            if(!a.isEmpty()) return ResponseUtils.createResponse(200, a);
            else             return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getApiById(@PathParam("id") Long id) throws JSONException {
        if(id == null) return ResponseUtils.createResponse(400, "");
        try {
            Api a = ApiService.findApiById(id);
            logger.debug("api = [" + a + "]");
            if(a != null)   return ResponseUtils.createResponse(200, a);
            else            return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, new JSONObject("{\"err\": 500, \"data\": \"La récupération a échoué\"}"));
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getCompactList() throws JSONException {
        try {
            List<String> apis = ApiService.getCompactNameList();
            logger.debug(apis.toString());
            return ResponseUtils.createResponse(200, apis);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }
}
