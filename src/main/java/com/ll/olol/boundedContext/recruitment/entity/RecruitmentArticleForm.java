package com.ll.olol.boundedContext.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@DynamicUpdate
public class RecruitmentArticleForm {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @MapsId
    @OneToOne
    private RecruitmentArticle recruitmentArticle;

    private int dayNight;

    private Long recruitsNumbers;

    private String mountainName;

    private String mtAddress;

    private String localCode;

    private Long ageRange;

    private LocalDateTime startTime;

    private LocalDateTime courseTime;

    private Long durationOfTime;

    private String connectType;

    public String getDayNightToString() {
        if (dayNight == 1) {
            return "주";
        } else {
            return "야";
        }
    }

    public String getStartTimeToString() {
        if (this.startTime == null) {
            return "미정";
        }

        return startTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public String getCourseTimeToString() {
        if (this.courseTime == null) {
            return "미정";
        }

        return courseTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public void update(CreateForm createForm, String realMtAddress) {
        Long durationOfTime = createForm.getDurationOfTime();
        if (durationOfTime == null) {
            durationOfTime = 5L;
        }

        if (createForm.getStartTime() == null) {
            this.startTime = null;
            this.courseTime = null;

        } else {
            this.startTime = createForm.getStartTime();
            this.courseTime = createForm.getStartTime().plusHours(durationOfTime);
        }

        this.dayNight = createForm.getDayNight();
        this.recruitsNumbers = createForm.getRecruitsNumber();
        this.mountainName = createForm.getMountainName();
        this.mtAddress = realMtAddress;
        this.ageRange = createForm.getAgeRange();
        this.durationOfTime = durationOfTime;
    }
}
