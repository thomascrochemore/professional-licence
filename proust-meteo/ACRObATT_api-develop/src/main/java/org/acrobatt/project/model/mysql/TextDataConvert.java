package org.acrobatt.project.model.mysql;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "TEXT_DATA_CONVERT", uniqueConstraints = @UniqueConstraint(columnNames = {"api_textdata", "textdata_id", "api_id"}))
public class TextDataConvert {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "api_textdata")
    private String apiTextData;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private TextData textData;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "api_id")
    private Api api;

    public TextDataConvert(){}
    public TextDataConvert(String apiTextData,TextData modelTextData,Api api){
        setApiTextData(apiTextData);
        setModelTextData(modelTextData);
        setApi(api);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiTextData() {
        return apiTextData;
    }

    public void setApiTextData(String apiTextData) {
        this.apiTextData = apiTextData;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public TextData getModelTextData() {
        return textData;
    }
    public void setModelTextData(TextData textData) {
        this.textData = textData;
    }
}
