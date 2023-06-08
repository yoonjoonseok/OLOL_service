package com.ll.olol.boundedContext.api.hikingTrail;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class HikingTrail {
    private String id;//등산로id
    private String secLen;//구간거리
    private String catNam;//난이도
    private String downMin;//하행속도
    private String upMin;//상행속도
    private List<List<Double>> coordinates;//위도경도
}
