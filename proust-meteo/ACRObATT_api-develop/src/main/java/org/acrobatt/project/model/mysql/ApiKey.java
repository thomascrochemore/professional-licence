package org.acrobatt.project.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "API_KEY")
public class ApiKey {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "key_value")
    private String key_value;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "api_id")
    private Api api;

    public ApiKey() {}
    public ApiKey(String key_value) {
        this.key_value = key_value;
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public String getKey_value() {
        return key_value;
    }
    public void setKey_value(String key_value) {
        this.key_value = key_value;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }
}
