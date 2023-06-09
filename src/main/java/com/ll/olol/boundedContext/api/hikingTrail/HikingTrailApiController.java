package com.ll.olol.boundedContext.api.hikingTrail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HikingTrailApiController {
    private final HikingTrailApiClient hikingTrailApiClient;

    @GetMapping("/api/hikingTrail")
    @ResponseBody
    public List getHikingTrail(@RequestParam String mountainName, @RequestParam String localCode) {
        return hikingTrailApiClient.requestHikingTrail(mountainName, localCode);
    }
}