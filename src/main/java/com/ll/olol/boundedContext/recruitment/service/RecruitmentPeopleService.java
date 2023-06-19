package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.member.repository.RecruitmentPeopleRepository;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentAttend;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class RecruitmentPeopleService {

    private final RecruitmentPeopleRepository recruitmentPeopleRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
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

    @Transactional
    public void delete(RecruitmentPeople recruitmentPeople) {
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));
        recruitmentPeopleRepository.delete(recruitmentPeople);
    }

    public RecruitmentPeople findOne(Long id) {
        return recruitmentPeopleRepository.findById(id).get();
    }

    public List findByRecruitmentArticle(RecruitmentArticle recruitmentArticle) {
        return recruitmentPeopleRepository.findAllByRecruitmentArticle(recruitmentArticle);
    }

    @Transactional
    public void attend(RecruitmentPeople recruitmentPeople) {
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));
        recruitmentPeople.setAttend(true);
    }
}
