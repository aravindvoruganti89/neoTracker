package com.test.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by avorugan on 8/24/17.
 */
public class NearEarthObject {

    private JSONObject links;
    private String neoReferenceId;
    private String neoName;
    private String nasaUrl;
    private String absoluteMagnitude;
    private JSONObject estimatedDiameter;
    private Boolean hazardousNeo;
    private JSONObject orbitalData;
    private ArrayList<CloseApproachData> closeApproachDatas;

    @JsonProperty("links")
    public JSONObject getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(JSONObject links) {
        this.links = links;
    }

    @JsonProperty("neo_reference_id")
    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    @JsonProperty("neo_reference_id")
    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    @JsonProperty("name")
    public String getNeoName() {
        return neoName;
    }

    @JsonProperty("name")
    public void setNeoName(String neoName) {
        this.neoName = neoName;
    }

    @JsonProperty("nasa_jpl_url")
    public String getNasaUrl() {
        return nasaUrl;
    }

    @JsonProperty("nasa_jpl_url")
    public void setNasaUrl(String nasaUrl) {
        this.nasaUrl = nasaUrl;
    }

    @JsonProperty("absolute_magnitude_h")
    public String getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    @JsonProperty("absolute_magnitude_h")
    public void setAbsoluteMagnitude(String absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;
    }

    @JsonProperty("estimated_diameter")
    public JSONObject getEstimatedDiameter() {
        return estimatedDiameter;
    }

    @JsonProperty("estimated_diameter")
    public void setEstimatedDiameter(JSONObject estimatedDiameter) {
        this.estimatedDiameter = estimatedDiameter;
    }

    @JsonProperty("is_potentially_hazardous_asteroid")
    public Boolean getHazardousNeo() {
        return hazardousNeo;
    }

    @JsonProperty("is_potentially_hazardous_asteroid")
    public void setHazardousNeo(Boolean hazardousNeo) {
        this.hazardousNeo = hazardousNeo;
    }

    @JsonProperty("orbital_data")
    public JSONObject getOrbitalData() {
        return orbitalData;
    }

    @JsonProperty("orbital_data")
    public void setOrbitalData(JSONObject orbitalData) {
        this.orbitalData = orbitalData;
    }

    @JsonProperty("close_approach_data")
    public ArrayList<CloseApproachData> getCloseApproachDatas() {
        return closeApproachDatas;
    }

    @JsonProperty("close_approach_data")
    public void setCloseApproachDatas(ArrayList<CloseApproachData> closeApproachDatas) {
        this.closeApproachDatas = closeApproachDatas;
    }

    public static class CloseApproachData {
        private String closeApproachDate;
        private Long epochCloseApproachDate;
        private String orbitingBody;
        private JSONObject relativeVelocity;
        private JSONObject missDistance;

        @JsonProperty("close_approach_date")
        public String getCloseApproachDate() {
            return closeApproachDate;
        }

        @JsonProperty("close_approach_date")
        public void setCloseApproachDate(String closeAppraochDate) {
            this.closeApproachDate = closeAppraochDate;
        }

        @JsonProperty("epoch_date_close_approach")
        public Long getEpochCloseApproachDate() {
            return epochCloseApproachDate;
        }

        @JsonProperty("epoch_date_close_approach")
        public void setEpochCloseApproachDate(Long epochCloseApproachDate) {
            this.epochCloseApproachDate = epochCloseApproachDate;
        }

        @JsonProperty("orbiting_body")
        public String getOrbitingBody() {
            return orbitingBody;
        }

        @JsonProperty("orbiting_body")
        public void setOrbitingBody(String orbitingBody) {
            this.orbitingBody = orbitingBody;
        }

        @JsonProperty("relative_velocity")
        public JSONObject getRelativeVelocity() {
            return relativeVelocity;
        }

        @JsonProperty("relative_velocity")
        public void setRelativeVelocity(JSONObject relativeVelocity) {
            this.relativeVelocity = relativeVelocity;
        }

        @JsonProperty("miss_distance")
        public JSONObject getMissDistance() {
            return missDistance;
        }

        @JsonProperty("miss_distance")
        public void setMissDistance(JSONObject missDistance) {
            this.missDistance = missDistance;
        }
    }



}
