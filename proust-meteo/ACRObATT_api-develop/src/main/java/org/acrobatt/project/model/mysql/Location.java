package org.acrobatt.project.model.mysql;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Location")
@Table(name = "LOCATION", uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude"}))
public class Location implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private Country country;

    public Location() {}
    public Location(String city, Float latitude, Float longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Location(String city, Float latitude, Float longitude, Country country) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.setCountry(country);
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public Float getLatitude() {
        return latitude;
    }
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("{%1s (%2s°N, %3s°W)}",
                this.getCity(), this.getLatitude(), this.getLongitude());
    }
}
