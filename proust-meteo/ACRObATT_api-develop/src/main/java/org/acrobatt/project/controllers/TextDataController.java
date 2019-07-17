package org.acrobatt.project.controllers;

import org.acrobatt.project.model.mysql.TextData;
import org.acrobatt.project.services.TextDataService;
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

@Path("/textdata")
public class TextDataController {

    private static Logger logger = LogManager.getLogger(TextDataController.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllTextData() throws JSONException {
        try {
            List<TextData> p = TextDataService.findAllTextData();
            if(p.size() > 0) return ResponseUtils.createResponse(200, p);
            else             return ResponseUtils.createResponse(404, "{\"err\": 404, \"data\": \"Objet non trouvé\"}");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getTextDataById(@PathParam("id") Long id) throws JSONException {
        if(id == null) return ResponseUtils.createResponse(400, "");
        try {
            TextData p = TextDataService.findTextDataById(id);
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
            List<String> locs = TextDataService.getCompactNameList();
            logger.debug(locs.toString());
            return ResponseUtils.createResponse(200, locs);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return ResponseUtils.createResponse(500, "{\"err\": 500, \"data\": \"La récupération a échoué\"}");
        }
    }
}
