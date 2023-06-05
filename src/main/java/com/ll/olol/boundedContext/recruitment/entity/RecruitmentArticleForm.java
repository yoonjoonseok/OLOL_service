package com.ll.olol.boundedContext.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@Setter
public class RecruitmentArticleForm {
    @Id
    private Long id;
    @MapsId
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "RecruitmentArticle_ID")
    private RecruitmentArticle recruitmentArticle;

    private int dayNight;

    private Long recruitsNumbers;

    private String mountainName;

    private String mtAddress;

    private String localCode;

    private Long ageRange;

    private LocalDateTime startTime;

    private LocalDateTime courseTime;

    private String connectType;

    public String getDayNightToString() {
        if (dayNight == 1) {
            return "주";
        } else {
            return "야";
        }
    }

    public String getStartTimeToString() {
        return startTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public String getCourseTimeToString() {
        return courseTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
