package com.ll.olol.boundedContext.review.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.event.EventAfterCheckedRealParticipant;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.review.entity.Review;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import com.ll.olol.boundedContext.review.repository.ReviewMemberRepository;
import com.ll.olol.boundedContext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMemberRepository reviewMemberRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void write(int reviewTypeCode, int appointmentTimeCode, int mannerCode, Member me) {
        Review review = Review
                .builder()
                .reviewTypeCode(reviewTypeCode)
                .appointmentTimeCode(appointmentTimeCode)
                .mannerCode(mannerCode)
                .toMember(null)
                .fromMember(me)
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public RsData realParticipant(RecruitmentPeople recruitmentPeople) {

        publisher.publishEvent(new EventAfterCheckedRealParticipant(this, recruitmentPeople));
        return RsData.of("S-1", "보냄");
    }

    @Transactional
    public RsData createReviewMember(Member reviewer, RecruitmentArticle recruitmentArticle, RecruitmentPeople recruitmentPeople) {
        ReviewMember reviewMember = ReviewMember
                .builder()
                .reviewMember(reviewer)
                .recruitmentArticle(recruitmentArticle)
                .recruitmentPeople(recruitmentPeople)
                .build();

        reviewMemberRepository.save(reviewMember);
        return RsData.of("S-1", "reviewMember를 생성했습니다.");
    }

    public List<ReviewMember> findAllByRecruitmentArticle(RecruitmentArticle recruitmentArticle) {
        return reviewMemberRepository.findAllByRecruitmentArticle(recruitmentArticle);
    }

    public List<RecruitmentPeople> findRealParticipant(Member me, Member author, RecruitmentArticle recruitmentArticle) {

        List<RecruitmentPeople> recruitmentPeopleList = recruitmentArticle.getRecruitmentPeople();

        List<RecruitmentPeople> realParticipantList = new ArrayList<>();

//        List<Member> realParticipantMemberList = new ArrayList<>();

        List<ReviewMember> realParticipantMemberList = new ArrayList<>();

        for (RecruitmentPeople participant : recruitmentPeopleList) {
            if (participant.isRealParticipant()) realParticipantList.add(participant);
        }

        // Member용
//        for (RecruitmentPeople participant : recruitmentPeopleList) {
//            if (participant.isRealParticipant()) realParticipantMemberList.add(participant.getMember());
//        }

        // ReviewMember용
//        for (RecruitmentPeople participant : recruitmentPeopleList) {
//            if (participant.isRealParticipant()) realParticipantMemberList.add(participant.);
//        }

        realParticipantMemberList
                .stream()
                .filter(e -> !e.equals(me))
                .collect(Collectors.toList());


        return realParticipantList;
    }
}
