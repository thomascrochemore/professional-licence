package org.acrobatt.project.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "API_URL", uniqueConstraints = @UniqueConstraint(columnNames = {"url_ex", "is_forecast"}))
public class ApiURL {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "url_ex")
    private String url_ex;

    @Column(name= "is_forecast")
    private Boolean isForecast;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "api_id")
    private Api api;

    public ApiURL() {}
    public ApiURL(String url_ex, Boolean isForecast) {
        this.url_ex = url_ex;
        this.isForecast = isForecast;
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public String getUrl_ex() {
        return url_ex;
    }
    public void setUrl_ex(String url_ex) {
        this.url_ex = url_ex;
    }

    public Boolean getForecast() { return isForecast; }
    public void setForecast(Boolean forecast) { isForecast = forecast; }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }
}
