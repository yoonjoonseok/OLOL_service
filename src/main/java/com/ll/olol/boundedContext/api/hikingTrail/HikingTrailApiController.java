package com.ll.olol.boundedContext.api.hikingTrail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HikingTrailApiController {
    private final HikingTrailApiClient hikingTrailApiClient;

    @GetMapping("/api/hikingTrail")
    public String getHikingTrail(String mountainName, String localCode, Model model) {
        mountainName = "삼성산";
        localCode = "41171102";

        model.addAttribute("hikingTrails", hikingTrailApiClient.requestHikingTrail(mountainName, localCode));
        model.addAttribute("latitude", hikingTrailApiClient.requestLatitude(mountainName, localCode));
        model.addAttribute("longitude", hikingTrailApiClient.requestLongitude(mountainName, localCode));

        return "usr/api/hikingTrail";
    }
}