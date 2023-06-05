package com.ll.olol.boundedContext.api.localCode;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LocalCodeApiController {
    private final LocalCodeApiClient localCodeApiClient;

    @GetMapping("/api/LocalCode/{keyword}")
    public String get(@PathVariable String keyword) {
        return localCodeApiClient.requestLocalCode(keyword);
    }
}
