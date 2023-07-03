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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMemberRepository reviewMemberRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void write(int reviewTypeCode, int appointmentTimeCode, int mannerCode, Member me, Member reviewTarget, RecruitmentArticle recruitmentArticle) {
        Review review = Review
                .builder()
                .recruitmentArticle(recruitmentArticle)
                .reviewTypeCode(reviewTypeCode)
                .appointmentTimeCode(appointmentTimeCode)
                .mannerCode(mannerCode)
                .toMember(reviewTarget)
                .fromMember(me)
                .build();

        reviewRepository.save(review);
    }


    public Review findReviewWrite(Member reviewTarget, Member me, RecruitmentArticle recruitmentArticle) {
        Optional<Review> opReview = reviewRepository.findByRecruitmentArticleAndToMemberAndFromMember(recruitmentArticle, reviewTarget, me);

        if (opReview.isPresent()) {
            return opReview.get();
        }

        return null;
    }

//    public RsData<Review> canReviewWrite(Member reviewTarget, Member me, RecruitmentArticle recruitmentArticle) {
//        Optional<Review> OpReview = reviewRepository.findByRecruitmentArticleAndToMemberAndFromMember(recruitmentArticle, reviewTarget, me);
//
//        if (OpReview.isPresent()) {
//            return RsData.of("F-1", "작성한 리뷰가 있습니다.", OpReview.get());
//        }
//        return RsData.of("S-1", "리뷰 작성 가능", OpReview.get());
//    }

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

    public ReviewMember reviewMemberFindById(Long id) {
        return reviewMemberRepository.findById(id).get();
    }

    public List<RecruitmentPeople> findRealParticipant(Member me, Member author, RecruitmentArticle recruitmentArticle) {

        List<RecruitmentPeople> recruitmentPeopleList = recruitmentArticle.getRecruitmentPeople();

        List<RecruitmentPeople> realParticipantList = new ArrayList<>();

//        List<Member> realParticipantMemberList = new ArrayList<>();

        List<ReviewMember> realParticipantMemberList = new ArrayList<>();

        for (RecruitmentPeople participant : recruitmentPeopleList) {
            if (participant.isRealParticipant()) realParticipantList.add(participant);
        }

        realParticipantMemberList
                .stream()
                .filter(e -> !e.equals(me))
                .collect(Collectors.toList());


        return realParticipantList;
    }

    @Transactional
    public void setCourseTimeEnd(RecruitmentArticle recruitmentArticle) {
        recruitmentArticle.setCourseTimeEnd(true);
    }
}
