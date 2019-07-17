package org.acrobatt.project.model.mysql;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WEATHERDATA", indexes = {
        @Index(columnList = "inserted_at"),
        @Index(columnList = "is_forecast"),
        @Index(columnList = "inserted_at,is_forecast")
})
public class WeatherData {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Delay data_delay;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Location data_location;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Api data_api;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<DataValue> data_values = new HashSet<>();

    @Column(name = "is_forecast")
    private Boolean isForecast;

    @Column(name = "inserted_at", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inserted_at;

    public WeatherData() {}
    public WeatherData(Delay data_delay, Location data_location, Api data_api, Set<DataValue> data_values, Boolean isForecast) {
        this.data_delay = data_delay;
        this.data_location = data_location;
        this.data_api = data_api;
        this.isForecast = isForecast;
        this.setData_values(data_values);
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public Delay getData_delay() {
        return data_delay;
    }
    public void setData_delay(Delay data_delay) {
        this.data_delay = data_delay;
    }

    public Location getData_location() {
        return data_location;
    }
    public void setData_location(Location data_location) {
        this.data_location = data_location;
    }

    public Api getData_api() {
        return data_api;
    }
    public void setData_api(Api data_api) {
        this.data_api = data_api;
    }

    public Boolean getForecast() {
        return isForecast;
    }
    public void setForecast(Boolean forecast) {
        isForecast = forecast;
    }

    public Date getInserted_at() {
        return inserted_at;
    }
    public void setInserted_at(Date inserted_at) {
        this.inserted_at = inserted_at;
    }
    public Set<DataValue> getData_values() {
        return data_values;
    }
    public void setData_values(Set<DataValue> data_values) {
        this.data_values = data_values;
        for(DataValue d : data_values) {
            d.setWeather_data(this);
        }
    }
    public void addData_value(DataValue value){
        data_values.add(value);
        value.setWeather_data(this);

    }

    public boolean isEmpty(){
        return data_values == null || data_values.isEmpty();
    }
}

