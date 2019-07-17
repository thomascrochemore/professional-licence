package org.acrobatt.project.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "DATAVALUE", uniqueConstraints = @UniqueConstraint(columnNames = {"property_id", "weather_data_id"}), indexes = {
        @Index(columnList = "id"),
        @Index(columnList = "weather_data_id"),
        @Index(columnList = "property_id"),
        @Index(columnList = "id,weather_data_id"),
        @Index(columnList = "id,property_id")
})
public class DataValue {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Property property;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    private WeatherData weather_data;

    @Column(name = "value_dec")
    private Float value_decimal;

    public DataValue() {}
    public DataValue(Property property, WeatherData weather_data, Float value_dec) {
        this.property = property;
        this.weather_data = weather_data;
        this.value_decimal = value_dec;
    }
    public DataValue(Property property, WeatherData weather_data, Integer value_dec) {
        this.property = property;
        this.weather_data = weather_data;
        this.value_decimal = value_dec.floatValue();
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }
    public void setProperty(Property property) {
        this.property = property;
    }

    public WeatherData getWeather_data() {
        return weather_data;
    }
    public void setWeather_data(WeatherData weather_data) {
        this.weather_data = weather_data;
    }

    public Float getValue_decimal() {
        return value_decimal;
    }
    public void setValue_decimal(Float value_decimal) {
        this.value_decimal = value_decimal;
    }
}
