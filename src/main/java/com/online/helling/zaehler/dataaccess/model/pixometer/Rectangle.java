
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
        "id",
        "annotation",
        "angle",
        "center_x",
        "center_y",
        "height",
        "width",
        "sharpness",
        "confidence"
})
public class Rectangle {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("annotation")
    private Integer annotation;
    @JsonProperty("angle")
    private Double angle;
    @JsonProperty("center_x")
    private Integer centerX;
    @JsonProperty("center_y")
    private Integer centerY;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("sharpness")
    private Double sharpness;
    @JsonProperty("confidence")
    private Double confidence;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("annotation")
    public Integer getAnnotation() {
        return annotation;
    }

    @JsonProperty("annotation")
    public void setAnnotation(Integer annotation) {
        this.annotation = annotation;
    }

    @JsonProperty("angle")
    public Double getAngle() {
        return angle;
    }

    @JsonProperty("angle")
    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @JsonProperty("center_x")
    public Integer getCenterX() {
        return centerX;
    }

    @JsonProperty("center_x")
    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    @JsonProperty("center_y")
    public Integer getCenterY() {
        return centerY;
    }

    @JsonProperty("center_y")
    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonProperty("sharpness")
    public Double getSharpness() {
        return sharpness;
    }

    @JsonProperty("sharpness")
    public void setSharpness(Double sharpness) {
        this.sharpness = sharpness;
    }

    @JsonProperty("confidence")
    public Double getConfidence() {
        return confidence;
    }

    @JsonProperty("confidence")
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
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
