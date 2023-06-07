package com.ll.olol.boundedContext.api.hikingTrail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HikingTrailApiClient {
    private final RestTemplate restTemplate;

    public HikingTrailApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${custom.vWorldApi.key}")
    private String key;
    @Value("${custom.site.baseUrl}")
    private String domain;
    @Value("${api.url.hikingTrail}")
    private String url_base;

    public List<HikingTrail> requestHikingTrail(String mountainName, String localCode) {
        String url_getHikingTrail = url_base.formatted(key, domain, mountainName, localCode);
        return restTemplate.exchange(url_getHikingTrail, HttpMethod.GET, null, HikingTrailDTO.class).getBody().getHikingTrails();
    }

    public double requestLatitude(String mountainName, String localCode) {
        String url_getHikingTrail = url_base.formatted(key, domain, mountainName, localCode);
        return restTemplate.exchange(url_getHikingTrail, HttpMethod.GET, null, HikingTrailDTO.class).getBody().getLatitude();
    }

    public double requestLongitude(String mountainName, String localCode) {
        String url_getHikiㅎngTrail = url_base.formatted(key, domain, mountainName, localCode);
        return restTemplate.exchange(url_getHikingTrail, HttpMethod.GET, null, HikingTrailDTO.class).getBody().getLongitude();
    }
}
