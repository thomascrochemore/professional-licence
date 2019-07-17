package org.acrobatt.project.apiparsers;

import org.acrobatt.project.apiparsers.parsers.ApixuParser;
import org.acrobatt.project.apiparsers.parsers.DarkSkyParser;
import org.acrobatt.project.apiparsers.parsers.OpenWeatherMapParser;
import org.acrobatt.project.apiparsers.parsers.WundergroundParser;

import java.io.IOException;

/**
 * A parser factory.
 */
public class ApiParserFactory {

    private static ApiParserFactory instance = null;

    ApixuParser apixuParser = ApixuParser.getInstance();
    OpenWeatherMapParser openWeatherMapParser = OpenWeatherMapParser.getInstance();
    WundergroundParser wundergroundParser = WundergroundParser.getInstance();
    DarkSkyParser darkSkyParser = DarkSkyParser.getInstance();

    /**
     * Generates an instance of this factory
     * @return an instance of the factory
     */
    public static ApiParserFactory getInstance(){
        if(instance == null){
            instance = new ApiParserFactory();
        }
        return instance;
    }

    private ApiParserFactory(){}

    /**
     * Returns the appropriate parser depending on the name of an API
     * @param name the name of the API
     * @return the corresponding ApiParser
     * @throws IOException if the API was not found in the ApiConst class
     */
    public ApiParser getApiParser(String name) throws IOException {
        if(name.equals(ApiConst.APIXU)){
            return apixuParser;
        }
        if(name.equals(ApiConst.OPEN_WEATHER_MAP)){
            return openWeatherMapParser;
        }
        if(name.equals(ApiConst.WUNDERGROUND)){
            return wundergroundParser;
        }
        if(name.equals(ApiConst.DARK_SKY)){
            return darkSkyParser;
        }
        throw new IOException("Api "+name+" NOT FOUND");
    }

    /**
     * Checks if an API exists in ApiConst
     * @param name the name
     * @return true if it exists
     */
    public boolean apiParserExists(String name) {
        try{
            return getApiParser(name) != null;
        }catch(IOException e){
            return false;
        }
    }
}
