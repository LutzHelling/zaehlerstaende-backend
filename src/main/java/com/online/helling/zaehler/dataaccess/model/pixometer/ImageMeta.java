
package com.online.helling.zaehler.dataaccess.model.pixometer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "annotations",
        "image",
        "image_download",
        "id",
        "camera_model",
        "flash",
        "frame_number",
        "seconds_since_detection",
        "seconds_since_start",
        "lat",
        "lng",
        "os_version",
        "pixolus_version"
})
public class ImageMeta {

    @JsonProperty("annotations")
    private List<Annotation> annotations = null;
    @JsonProperty("image")
    private String image;
    @JsonProperty("image_download")
    private String imageDownload;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("camera_model")
    private String cameraModel;
    @JsonProperty("flash")
    private Boolean flash;
    @JsonProperty("frame_number")
    private Integer frameNumber;
    @JsonProperty("seconds_since_detection")
    private Double secondsSinceDetection;
    @JsonProperty("seconds_since_start")
    private Double secondsSinceStart;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
    @JsonProperty("os_version")
    private String osVersion;
    @JsonProperty("pixolus_version")
    private String pixolusVersion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("annotations")
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @JsonProperty("annotations")
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("image_download")
    public String getImageDownload() {
        return imageDownload;
    }

    @JsonProperty("image_download")
    public void setImageDownload(String imageDownload) {
        this.imageDownload = imageDownload;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("camera_model")
    public String getCameraModel() {
        return cameraModel;
    }

    @JsonProperty("camera_model")
    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    @JsonProperty("flash")
    public Boolean getFlash() {
        return flash;
    }

    @JsonProperty("flash")
    public void setFlash(Boolean flash) {
        this.flash = flash;
    }

    @JsonProperty("frame_number")
    public Integer getFrameNumber() {
        return frameNumber;
    }

    @JsonProperty("frame_number")
    public void setFrameNumber(Integer frameNumber) {
        this.frameNumber = frameNumber;
    }

    @JsonProperty("seconds_since_detection")
    public Double getSecondsSinceDetection() {
        return secondsSinceDetection;
    }

    @JsonProperty("seconds_since_detection")
    public void setSecondsSinceDetection(Double secondsSinceDetection) {
        this.secondsSinceDetection = secondsSinceDetection;
    }

    @JsonProperty("seconds_since_start")
    public Double getSecondsSinceStart() {
        return secondsSinceStart;
    }

    @JsonProperty("seconds_since_start")
    public void setSecondsSinceStart(Double secondsSinceStart) {
        this.secondsSinceStart = secondsSinceStart;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("lng")
    public Double getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @JsonProperty("os_version")
    public String getOsVersion() {
        return osVersion;
    }

    @JsonProperty("os_version")
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @JsonProperty("pixolus_version")
    public String getPixolusVersion() {
        return pixolusVersion;
    }

    @JsonProperty("pixolus_version")
    public void setPixolusVersion(String pixolusVersion) {
        this.pixolusVersion = pixolusVersion;
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
