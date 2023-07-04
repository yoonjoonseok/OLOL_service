package com.ll.olol.boundedContext.recruitment.entity;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateForm {
    //        @NotBlank
//        @Size(min = 3, max = 65)
    @NotBlank(message = "제목은 필수항목입니다.") // 아래의 변수는 비어있으면 안된다.
    @Size(max = 65, message = "제목을 200자 이하로 설정해주세요.") // 최대 200까지 가능하다.
    private String articleName;


    @Size(max = 100000, message = "제목을 20000자 이하로 설정해주세요.")
    private String content;


    @NotNull(message = "타입은 필수항목입니다.")
    @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
    @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
    private Integer typeValue;

    @NotNull(message = "타입은 필수항목입니다.")
    @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
    @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
    private Integer dayNight;

    private String mountainName;

    private String mtAddress;

    @NotNull(message = "모집인원은 필수항목입니다.")
    @Min(value = 0, message = "올바른 인원수를 입력해주세요.")
    private Long recruitsNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime courseTime;

    @Positive(message = "올바른 시간을 입력해주세요.")
    private Long durationOfTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "마감일 지정은 필수항목입니다.")
    private LocalDateTime deadLineDate;

    private Long ageRange;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String connectType;

    @AssertTrue(message = "시작 시간은 마감 시간보다 늦어야 합니다.")
    public boolean isStartTimeAfterDeadLineDate() {
        if (startTime == null || deadLineDate == null) {
            return true;  // 필드 중 하나가 null이면 검증 통과
        }
        return startTime.isAfter(deadLineDate);
    }

    public void set(RecruitmentArticle recruitmentArticle) {
        RecruitmentArticleForm recruitmentArticleForm = recruitmentArticle.getRecruitmentArticleForm();

        this.articleName = recruitmentArticle.getArticleName();
        this.content = recruitmentArticle.getContent();
        this.typeValue = recruitmentArticle.getTypeValue();
        this.dayNight = recruitmentArticleForm.getDayNight();
        this.mountainName = recruitmentArticleForm.getMountainName();
        this.mtAddress = recruitmentArticleForm.getMtAddress();
        this.recruitsNumber = recruitmentArticleForm.getRecruitsNumbers();
        this.startTime = recruitmentArticleForm.getStartTime();
        this.courseTime = recruitmentArticleForm.getCourseTime();
        this.durationOfTime = recruitmentArticleForm.getDurationOfTime();
        this.deadLineDate = recruitmentArticle.getDeadLineDate();
        this.ageRange = recruitmentArticleForm.getAgeRange();
        this.connectType = recruitmentArticleForm.getConnectType();
    }
}
