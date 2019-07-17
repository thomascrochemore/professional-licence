package org.acrobatt.project.model.mysql.analysis;

import org.acrobatt.project.model.mysql.Location;
import org.acrobatt.project.model.mysql.Property;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="stats_avg_rt")
public class AverageDataRealtime implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name="date_req")
    private Date date_req;

    @OneToOne(cascade = {CascadeType.REFRESH})
    private Property prop;

    @OneToOne(cascade = {CascadeType.REFRESH})
    private Location location;

    @Column(name="avg_rt")
    private Float average;

    public AverageDataRealtime() {}
    public AverageDataRealtime(Property property, Location location, Float average) {
        this.prop = property;
        this.location = location;
        this.average = average;
    }

    public Long getId() { return id; }
    private void setId(Long id) { this.id = id; }

    public Property getProp() { return prop; }
    public void setProp(Property property) { this.prop = property; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Float getAverage() { return average; }
    public void setAverage(Float average) { this.average = average; }
}
