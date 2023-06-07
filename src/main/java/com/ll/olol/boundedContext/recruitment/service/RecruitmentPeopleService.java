package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentPeopleRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecruitmentPeopleService {
    @Autowired
    private final RecruitmentPeopleRepository recruitmentPeopleRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RecruitmentRepository recruitmentRepository;

    public Long saveRecruitmentPeople(Long memberId, Long recruitmentId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<RecruitmentArticle> article = recruitmentRepository.findById(recruitmentId);
        RecruitmentPeople recruitmentPeople = new RecruitmentPeople();
        recruitmentPeople.setMember(member.get());
        recruitmentPeople.setRecruitmentArticle(article.get());

        RecruitmentPeople saved = recruitmentPeopleRepository.save(recruitmentPeople);
        return saved.getId();
    }
}
