package org.acrobatt.project.model.mysql.analysis;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Immutable
@Table(name = "stats_values_distance")
public class ComparativeData {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private Date date_req;
    private String api;
    private String prop;
    private String location;
    private Integer delay;
    private Float value_fcst;
    private Float value_rt;
    private Float distance;

    public Date getDate_req() { return date_req; }
    public String getApi() { return api; }
    public String getProp() { return prop; }
    public String getLocation() { return location; }
    public Integer getDelay() { return delay; }

    public Float getValue_fcst() { return value_fcst; }
    public Float getValue_rt() { return value_rt; }
    public Float getDistance() { return distance; }
}
