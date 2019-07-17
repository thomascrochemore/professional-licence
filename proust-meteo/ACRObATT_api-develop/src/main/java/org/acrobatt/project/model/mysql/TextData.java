package org.acrobatt.project.model.mysql;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "TEXTDATA", uniqueConstraints = @UniqueConstraint(columnNames = {"text", "intensity", "property_id"}))
public class TextData {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "intensity")
    private Integer intensity;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private Property property;

    public TextData() {}
    public TextData(String text, Integer intensity, Property property) {
        this.text = text;
        this.intensity = intensity;
        this.property = property;
    }

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Integer getIntensity() {
        return intensity;
    }
    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public Property getProperty() {
        return property;
    }
    public void setProperty(Property property) {
        this.property = property;
    }
}
