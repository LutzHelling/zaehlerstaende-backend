
package com.online.helling.zaehler.dataaccess.model.pixometer;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rectangle",
        "id",
        "meaning",
        "text",
        "image"
})
public class Annotation {

    @JsonProperty("rectangle")
    private Rectangle rectangle;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("meaning")
    private String meaning;
    @JsonProperty("text")
    private String text;
    @JsonProperty("image")
    private Integer image;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("rectangle")
    public Rectangle getRectangle() {
        return rectangle;
    }

    @JsonProperty("rectangle")
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("meaning")
    public String getMeaning() {
        return meaning;
    }

    @JsonProperty("meaning")
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("image")
    public Integer getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Integer image) {
        this.image = image;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
