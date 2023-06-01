package com.ll.olol.boundedContext.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    @OneToOne
    @JoinColumn(name="RecruitmentArticle_ID")
    private RecruitmentArticle recruitmentArticle;

    private int dayNight;

    private Long recruitsNumbers;

    private String mountainName;

    private Long ageRange;

    private LocalDateTime startTime;

    private LocalDateTime courseTime;

    private String ConnectType;

}
