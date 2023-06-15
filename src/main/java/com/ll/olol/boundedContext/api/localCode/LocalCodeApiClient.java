package com.ll.olol.boundedContext.api.localCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocalCodeApiClient {
    private final RestTemplate restTemplate;

    public LocalCodeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${custom.vWorldApi.key}")
    private String key;
    @Value("${custom.site.baseUrl}")
    private String domain;
    @Value("${api.url.localCode}")
    private String url_getLocalCode;

    public String requestLocalCode(String keyword) {
        url_getLocalCode = url_getLocalCode.formatted(key, domain);

        //return restTemplate.exchange(url_getLocalCode, HttpMethod.GET, null, LocalCodeDTO.class, keyword).getBody().getEmdCd();
        String result = "";
        try {
            result = restTemplate.exchange(url_getLocalCode, HttpMethod.GET, null, LocalCodeDTO.class, keyword).getBody().getEmdCd();
        } catch (Exception e) {
        }
        return result;
    }
}
