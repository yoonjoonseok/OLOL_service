package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentAttend;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentPeopleRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RecruitmentPeopleService {

    private final RecruitmentPeopleRepository recruitmentPeopleRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicationEventPublisher publisher;

    public Long saveRecruitmentPeople(Long memberId, Long recruitmentId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<RecruitmentArticle> article = recruitmentRepository.findById(recruitmentId);
        RecruitmentPeople recruitmentPeople = new RecruitmentPeople();
        recruitmentPeople.setMember(member.get());
        recruitmentPeople.setRecruitmentArticle(article.get());

        RecruitmentPeople saved = recruitmentPeopleRepository.save(recruitmentPeople);
        publisher.publishEvent(new EventAfterRecruitmentPeople(this, saved));
        return saved.getId();
    }

    public void delete(RecruitmentPeople recruitmentPeople) {
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));
        recruitmentPeopleRepository.delete(recruitmentPeople);
    }

    public RecruitmentPeople findOne(Long id) {
        return recruitmentPeopleRepository.findById(id).get();
    }

    @Transactional
    public void attend(RecruitmentPeople recruitmentPeople) {
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));
        recruitmentPeople.setAttend(true);
    }
}
