
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
        "applying_person",
        "image_meta",
        "image_second_tariff_meta",
        "meter",
        "url",
        "resource_id",
        "changed_hash",
        "created",
        "modified",
        "applied_method",
        "reading_date",
        "value",
        "value_second_tariff",
        "provided_fraction_digits",
        "provided_fraction_digits_second_tariff"
})
public class Result {

    @JsonProperty("applying_person")
    private String applyingPerson;
    @JsonProperty("image_meta")
    private ImageMeta imageMeta;
    @JsonProperty("image_second_tariff_meta")
    private Object imageSecondTariffMeta;
    @JsonProperty("meter")
    private String meter;
    @JsonProperty("url")
    private String url;
    @JsonProperty("resource_id")
    private Integer resourceId;
    @JsonProperty("changed_hash")
    private String changedHash;
    @JsonProperty("created")
    private String created;
    @JsonProperty("modified")
    private String modified;
    @JsonProperty("applied_method")
    private String appliedMethod;
    @JsonProperty("reading_date")
    private String readingDate;
    @JsonProperty("value")
    private String value;
    @JsonProperty("value_second_tariff")
    private String valueSecondTariff;
    @JsonProperty("provided_fraction_digits")
    private Integer providedFractionDigits;
    @JsonProperty("provided_fraction_digits_second_tariff")
    private Integer providedFractionDigitsSecondTariff;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("applying_person")
    public String getApplyingPerson() {
        return applyingPerson;
    }

    @JsonProperty("applying_person")
    public void setApplyingPerson(String applyingPerson) {
        this.applyingPerson = applyingPerson;
    }

    @JsonProperty("image_meta")
    public ImageMeta getImageMeta() {
        return imageMeta;
    }

    @JsonProperty("image_meta")
    public void setImageMeta(ImageMeta imageMeta) {
        this.imageMeta = imageMeta;
    }

    @JsonProperty("image_second_tariff_meta")
    public Object getImageSecondTariffMeta() {
        return imageSecondTariffMeta;
    }

    @JsonProperty("image_second_tariff_meta")
    public void setImageSecondTariffMeta(Object imageSecondTariffMeta) {
        this.imageSecondTariffMeta = imageSecondTariffMeta;
    }

    @JsonProperty("meter")
    public String getMeter() {
        return meter;
    }

    @JsonProperty("meter")
    public void setMeter(String meter) {
        this.meter = meter;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("resource_id")
    public Integer getResourceId() {
        return resourceId;
    }

    @JsonProperty("resource_id")
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @JsonProperty("changed_hash")
    public String getChangedHash() {
        return changedHash;
    }

    @JsonProperty("changed_hash")
    public void setChangedHash(String changedHash) {
        this.changedHash = changedHash;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("modified")
    public String getModified() {
        return modified;
    }

    @JsonProperty("modified")
    public void setModified(String modified) {
        this.modified = modified;
    }

    @JsonProperty("applied_method")
    public String getAppliedMethod() {
        return appliedMethod;
    }

    @JsonProperty("applied_method")
    public void setAppliedMethod(String appliedMethod) {
        this.appliedMethod = appliedMethod;
    }

    @JsonProperty("reading_date")
    public String getReadingDate() {
        return readingDate;
    }

    @JsonProperty("reading_date")
    public void setReadingDate(String readingDate) {
        this.readingDate = readingDate;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("value_second_tariff")
    public String getValueSecondTariff() {
        return valueSecondTariff;
    }

    @JsonProperty("value_second_tariff")
    public void setValueSecondTariff(String valueSecondTariff) {
        this.valueSecondTariff = valueSecondTariff;
    }

    @JsonProperty("provided_fraction_digits")
    public Integer getProvidedFractionDigits() {
        return providedFractionDigits;
    }

    @JsonProperty("provided_fraction_digits")
    public void setProvidedFractionDigits(Integer providedFractionDigits) {
        this.providedFractionDigits = providedFractionDigits;
    }

    @JsonProperty("provided_fraction_digits_second_tariff")
    public Integer getProvidedFractionDigitsSecondTariff() {
        return providedFractionDigitsSecondTariff;
    }

    @JsonProperty("provided_fraction_digits_second_tariff")
    public void setProvidedFractionDigitsSecondTariff(Integer providedFractionDigitsSecondTariff) {
        this.providedFractionDigitsSecondTariff = providedFractionDigitsSecondTariff;
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
