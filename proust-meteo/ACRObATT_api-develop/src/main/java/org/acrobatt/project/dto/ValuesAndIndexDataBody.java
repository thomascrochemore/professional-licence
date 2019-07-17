package org.acrobatt.project.dto;

import javax.ws.rs.QueryParam;

public class ValuesAndIndexDataBody {
    @QueryParam("delay") public Integer delay;
    @QueryParam("city") public String city;
    @QueryParam("property") public String property;
}
