package org.acrobatt.project.model.mysql.cache;

import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.model.mysql.Location;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CACHEREQUEST")
public class CacheRequest {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Api api;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Location location;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Delay delay;

    private Date date_request;

    public CacheRequest(){}

    public CacheRequest(Api api,Location location,Delay delay,Date date_request){
        setApi(api);
        setLocation(location);
        setDelay(delay);
        setDate_request(date_request);
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Delay getDelay() {
        return delay;
    }

    public void setDelay(Delay delay) {
        this.delay = delay;
    }

    public Date getDate_request() {
        return date_request;
    }

    public void setDate_request(Date date_request) {
        this.date_request = date_request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
