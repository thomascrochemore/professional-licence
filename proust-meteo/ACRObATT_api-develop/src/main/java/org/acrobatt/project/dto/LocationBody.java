package org.acrobatt.project.dto;

import javax.ws.rs.QueryParam;

/**
 * A request body used to work with locations.
 * This body is used as a BeanParam in Jersey resource classes.
 */
public class LocationBody {
    @QueryParam("latitude")     public Float latitude;
    @QueryParam("longitude")    public Float longitude;
    @QueryParam("country")      public String country;
    @QueryParam("city")         public String cityName;
}
