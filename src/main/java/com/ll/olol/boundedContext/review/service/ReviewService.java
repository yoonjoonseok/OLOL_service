package com.ll.olol.boundedContext.review.service;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.review.entity.Review;
import com.ll.olol.boundedContext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

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
}
