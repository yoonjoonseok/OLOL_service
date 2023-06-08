package com.ll.olol.boundedContext.api.hikingTrail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HikingTrailDTO {

    @JsonProperty("response")
    private Response response;

    public static class Response {
        @JsonProperty("result")
        private Result result;
    }

    public static class Result {
        @JsonProperty("featureCollection")
        private Featurecollection featurecollection;
    }

    public static class Featurecollection {
        @JsonProperty("features")
        private List<Features> features;
        @JsonProperty("bbox")
        private List<Double> bbox;
    }

    public static class Features {
        @JsonProperty("id")
        private String id;
        @JsonProperty("properties")
        private Properties properties;
        @JsonProperty("geometry")
        private Geometry geometry;
    }

    public static class Properties {
        @JsonProperty("mntn_nm")
        private String mntnNm;
        @JsonProperty("sec_len")
        private String secLen;
        @JsonProperty("cat_nam")
        private String catNam;
        @JsonProperty("down_min")
        private String downMin;
        @JsonProperty("up_min")
        private String upMin;
    }

    public static class Geometry {
        @JsonProperty("coordinates")
        private List<List<List<Double>>> coordinates;
    }

    public List<HikingTrail> getHikingTrails() {
        List<HikingTrail> hikingTrails = new ArrayList<>();
        List<Features> features = response.result.featurecollection.features;

        for (int i = 0; i < features.size(); i++) {
            Features feature = features.get(i);
            Properties properties = feature.properties;
            List<List<List<Double>>> coordinates = feature.geometry.coordinates;

            List<List<Double>> newList = coordinates.stream()
                    .flatMap(List::stream)
                    .map(ArrayList::new)
                    .collect(Collectors.toList());

            HikingTrail hikingTrail = HikingTrail.builder()
                    .id(feature.id)
                    .secLen(properties.secLen)
                    .catNam(properties.catNam)
                    .downMin(properties.downMin)
                    .upMin(properties.upMin)
                    .coordinates(newList)
                    .build();

            hikingTrails.add(hikingTrail);
        }
        return hikingTrails;
    }

    public double getLatitude() {
        List<Double> bbox = response.result.featurecollection.bbox;

        return (bbox.get(1) + bbox.get(3)) / 2;
    }

    public double getLongitude() {
        List<Double> bbox = response.result.featurecollection.bbox;

        return (bbox.get(0) + bbox.get(2)) / 2;
    }
}
