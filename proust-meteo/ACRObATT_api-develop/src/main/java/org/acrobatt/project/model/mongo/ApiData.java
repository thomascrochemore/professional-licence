package org.acrobatt.project.model.mongo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.json.JSONObject;

import javax.persistence.Id;
import java.util.Date;

public class ApiData {
    public enum Type{FORECAST,RESULT}

    @Id
    @JsonProperty("_id")
    private String id;

    private String city;

    private String country;

    private Date storedDate;

    private Integer delay;

    private String api;

    private JSONObject data;

    public ApiData(){}

    public ApiData(JSONObject data, String city,String api,Integer delay,Date storedDate){
        setData(data);
        setCity(city);
        setApi(api);
        setDelay(delay);
        setStoredDate(storedDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getStoredDate() {
        return storedDate;
    }

    public void setStoredDate(Date storedDate) {
        this.storedDate = storedDate;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @JsonIgnore
    public Type getType(){
        return delay == 0 ? Type.RESULT : Type.FORECAST;
    }


    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    @JsonGetter
    public JSONObject getData() {
        return data;
    }

    @JsonSetter
    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
