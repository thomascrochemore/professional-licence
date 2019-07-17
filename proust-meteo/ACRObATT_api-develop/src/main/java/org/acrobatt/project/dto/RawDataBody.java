package org.acrobatt.project.dto;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * A request body used to work with WeatherData objects and their equivalents (ComparativeData).
 * This body is used as a BeanParam in Jersey resource classes.
 */
public class RawDataBody {
    @QueryParam("delay") @DefaultValue("0")                 public Integer delay;
    @QueryParam("forecast") @DefaultValue("false")          public Boolean isforecast;
    @QueryParam("datetime")                                 public String datetime;
    @QueryParam("datetime_to")                              public String datetime_to;
    @QueryParam("api")                                      public String api;
    @QueryParam("city")                                     public String city;
    @QueryParam("property")                                 public String property;
}
