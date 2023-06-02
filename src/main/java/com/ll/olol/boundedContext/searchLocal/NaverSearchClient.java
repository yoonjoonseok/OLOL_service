package com.ll.olol.boundedContext.searchLocal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class NaverSearchClient {

    @Value("${custom.localApi.naver.id}")
    private String naverClientId;

    @Value("${custom.localApi.naver.secret}")
    private String naverClientSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;


    public static <T> ResponseEntity<T> search(MultiValueMap<String, String> mv,
                                               ParameterizedTypeReference<T> responseType,
                                               String url, String id, String passwd) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParams(mv)
                .build()
                .encode()
                .toUri();

        HttpHeaders header = getHttpHeaders(id, passwd, MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(header);

        var responseEntity = new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity;
    }

    private static HttpHeaders getHttpHeaders(String id, String passwd, MediaType contentType) {
        HttpHeaders header = new HttpHeaders();
        header.set("X-Naver-Client-Id", id);
        header.set("X-Naver-Client-Secret", passwd);
        header.setContentType(contentType);
        return header;
    }

    public SearchLocalRes searchLocal(SearchLocalReq searchLocalReq) {
        var mv = searchLocalReq.toMultiValueMap();
        var responseType = new ParameterizedTypeReference<SearchLocalRes>() {
        };
        var responseEntity = search(mv, responseType, naverLocalSearchUrl, naverClientId, naverClientSecret);

        return responseEntity.getBody();
    }
}
