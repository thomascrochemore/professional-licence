package org.acrobatt.project.controllers;

import org.acrobatt.project.dao.mysql.ApiDAO;
import org.acrobatt.project.dto.RawDataBody;
import org.acrobatt.project.dto.ValuesAndIndexDataBody;
import org.acrobatt.project.dto.WidgetDataBody;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.services.rawdata.DataHubService;
import org.acrobatt.project.utils.ResponseUtils;
import org.acrobatt.project.utils.enums.DataOriginType;
import org.acrobatt.project.utils.enums.DataProcess;
import org.acrobatt.project.weatherapi.WeatherApiService;
import org.acrobatt.project.weatherdata.WeatherDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Path("/weatherdata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherDataController {

    private static Logger logger = LogManager.getLogger(WeatherDataController.class);

    private WeatherDataService weatherDataService = WeatherDataService.getInstance();
    private DataHubService dataHubService = DataHubService.getInstance();


    public WeatherDataController() throws IOException {}

    @GET
    @Path("/compute")
    public Response computeWeatherData() throws IOException, JSONException {
        weatherDataService.computeApisData();
        return Response.ok().entity("{\"message\":\"success\"}").build();
    }

    @GET
    @Path("/backup")
    public Response backupWeatherData() throws IOException {
        weatherDataService.backupApiDataToWeatherData();
        return Response.ok().entity("{\"message\":\"success\"}").build();
    }

    @GET
    @Path("/rawdata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBulkWeatherData(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.MODULAR_ENTRY, DataOriginType.MODULAR);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requis: [datetime, delay (si positif & non nul)] & exactement 1 dans: [api, city, property]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/compare")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComparativeDataPeriod(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.MODULAR_ENTRY, DataOriginType.COMPARATIVE);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requis: [datetime, datetime_to, city, property, delay]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/distance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistanceData(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.SINGLE_ENTRY, DataOriginType.DISTANCE);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requis: [datetime, datetime_to, api, city, property, delay > 0]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/score")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScoreData(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.SINGLE_ENTRY, DataOriginType.SCORE);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requis: [datetime, datetime_to, city, property, delay]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/eval")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIndexData(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.SINGLE_ENTRY, DataOriginType.INDEX);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requiert exactement 3 dans: [api, city, property, delay (positif)]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/evalDetailed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailedIndexData(@BeanParam RawDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getStoredData(body, DataProcess.SINGLE_ENTRY, DataOriginType.DETAILED_INDEX);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch(ParseException e) {
            logger.error("Parsing error : "+ e.getMessage()+"\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Format de date invalide\" }");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requiert exactement 3 dans: [api, city, property, delay (positif)]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }


    @GET
    @Path("/widget")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptimizedWidgetData(@BeanParam WidgetDataBody body) {
        try {
            JSONObject bulkData = dataHubService.getWidgetData(body);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200, bulkData.toString());

        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \"Paramètre(s) invalide(s), requis: [city] & un/plusieurs/aucun dans [property, delay > 0]\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console\" }");
        }
    }

    @GET
    @Path("/values-index")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValuesAndIndexData(@BeanParam ValuesAndIndexDataBody body){
        try {
            JSONObject bulkData = dataHubService.getValuesAndIndexes(body);
            if(bulkData == null) throw new NoResultException();
            return ResponseUtils.createResponse(200,bulkData.toString());
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument, check request ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(400, "{ \"err\": 400, \"message\": \""+e.getMessage()+"\" }");
        } catch (NoResultException e) {
            logger.warn("No result found in database\n", e);
            return ResponseUtils.createResponse(200, "{\"data\": \"Aucune donnée trouvée\"}");
        } catch (Exception e) {
            logger.error("Unreported exception occurred : "+ e.getClass().getName() + " ("+e.getMessage()+")\n", e);
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur interne. Consultez la console "+ ExceptionUtils.exceptionStackTraceAsString(e)+"\" }");
        }
    }

    @GET
    @Path("/test-api")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataApi() throws IOException, JSONException {
        WeatherApiService weatherApiService = WeatherApiService.getInstance();
        List<ApiData> apiDataList = weatherApiService.getAllApiData(Date.from(Instant.now()));
        return ResponseUtils.createResponse(200,apiDataList);
    }
}
