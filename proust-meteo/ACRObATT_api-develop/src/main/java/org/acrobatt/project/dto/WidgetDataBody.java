package org.acrobatt.project.dto;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * A request body used to work with widget data.
 * This body is used as a BeanParam in Jersey resource classes.
 */
public class WidgetDataBody {
    @QueryParam("delay")                                    public Integer delay;
    @NotNull @QueryParam("datetime")                        public String datetime;
    @QueryParam("api")                                      public String api;
    @QueryParam("city")                                     public String city;
    @QueryParam("property")                                 public List<String> property;
}
