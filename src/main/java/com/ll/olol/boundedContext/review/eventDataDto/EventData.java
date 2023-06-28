package com.ll.olol.boundedContext.review.eventDataDto;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import lombok.*;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private RecruitmentPeople recruitmentPeople;

    public void setRecruitmentPeople(RecruitmentPeople recruitmentPeople) {
        this.recruitmentPeople = recruitmentPeople;
    }
}
