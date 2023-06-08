package com.ll.olol.boundedContext.api.localCode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LocalCodeDTO {
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
    }

    public static class Features {
        @JsonProperty("properties")
        private Properties properties;
    }

    public static class Properties {
        @JsonProperty("emd_cd")
        private String emdCd;
    }

    public String getEmdCd() {
        return response.result.featurecollection.features.get(0).properties.emdCd;
    }
}
