package org.acrobatt.project.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "API")
public class Api {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "last_used_key")
    private Long lastUsedKey = 0l;

    @Column(name = "api_name", unique = true)
    private String name;

    @Column(name="req_per_hour")
    private Long reqPerHour;

    @Column(name = "req_per_day")
    private Long reqPerDay;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "api")
    private List<ApiKey> api_keys;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "api")
    private Set<ApiURL> url_schemes;

    public Api() {}
    public Api(String name, List<ApiKey> keys, Set<ApiURL> schemes) {
        this.name = name;
        this.setApi_keys(keys);
        this.setUrl_schemes(schemes);
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<ApiKey> getApi_keys() {
        return api_keys;
    }
    public void setApi_keys(List<ApiKey> api_keys) {
        for(ApiKey key : api_keys){
            key.setApi(this);
        }
        this.api_keys = api_keys;
    }

    public Set<ApiURL> getUrl_schemes() {
        return url_schemes;
    }
    public void setUrl_schemes(Set<ApiURL> url_schemes) {
        for(ApiURL url : url_schemes){
            url.setApi(this);
        }
        this.url_schemes = url_schemes;
    }

    public Long getLastUsedKey() {
        return lastUsedKey;
    }

    public void setLastUsedKey(Long lastUsedKey) {
        this.lastUsedKey = lastUsedKey;
    }

    public ApiURL getForecastUrl(){
        for(ApiURL apiURL : getUrl_schemes()){
            if(apiURL.getForecast()){
                return apiURL;
            }
        }
        return null;
    }
    public ApiURL getRealtimeUrl(){
        for(ApiURL apiURL : getUrl_schemes()){
            if(!apiURL.getForecast()){
                return apiURL;
            }
        }
        return null;
    }

    public Long getReqPerHour() {
        return reqPerHour;
    }

    public void setReqPerHour(Long reqPerHour) {
        this.reqPerHour = reqPerHour;
    }

    public Long getReqPerDay() {
        return reqPerDay;
    }

    public void setReqPerDay(Long reqPerDay) {
        this.reqPerDay = reqPerDay;
    }
}
