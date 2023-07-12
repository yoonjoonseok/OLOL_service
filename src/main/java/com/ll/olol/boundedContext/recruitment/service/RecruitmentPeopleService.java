package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.notification.event.EventAfterDeportPeople;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentAttend;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentPeopleRepository;
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
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicationEventPublisher publisher;
    private final MemberService memberService;
    private final Rq rq;
    private final int limitPeople = 0;

    @Transactional
    public RsData saveRecruitmentPeople(Member member, Long recruitmentId) {

        if (member == null) {
            return RsData.of("F-1", "회원 정보가 없습니다.");
        }
        Optional<RecruitmentArticle> article = recruitmentRepository.findById(recruitmentId);

        Optional<RecruitmentPeople> first = article.get().getRecruitmentPeople().stream()
                .filter(t -> t.getMember().getId() == member.getId())
                .findFirst();
        if (memberService.additionalInfo(rq.getMember()).isFail()) {
            return RsData.of("F-2", "마이페이지에서 추가정보를 입력해주세요");
        }

        if (first.isPresent()) {
            return RsData.of("F-1", "이미 신청한 공고입니다.");
        }

        if (article.get().getMember().getId() == member.getId()) {
            return RsData.of("F-1", "게시글을 작성한 사람은 참가를 누를 수 없습니다.");
        }
        RecruitmentPeople recruitmentPeople = new RecruitmentPeople();
        recruitmentPeople.setMember(member);
        recruitmentPeople.setRecruitmentArticle(article.get());

        RecruitmentPeople saved = recruitmentPeopleRepository.save(recruitmentPeople);
        publisher.publishEvent(new EventAfterRecruitmentPeople(this, saved));
        return RsData.of("S-1", "신청 성공");
    }

    @Transactional
    public RsData<Object> delete(RecruitmentPeople recruitmentPeople) {
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));
        recruitmentPeopleRepository.delete(recruitmentPeople);
        return RsData.of("S-1", "거절 완료");
    }

    public RecruitmentPeople findOne(Long id) {
        return recruitmentPeopleRepository.findById(id).get();
    }

    public List findByRecruitmentArticle(RecruitmentArticle recruitmentArticle) {
        return recruitmentPeopleRepository.findAllByRecruitmentArticle(recruitmentArticle);
    }

    @Transactional
    public RsData attend(RecruitmentPeople recruitmentPeople) {

        RecruitmentArticle recruitmentArticle = recruitmentPeople.getRecruitmentArticle();
        Long recruitsNumbers = recruitmentArticle.getRecruitmentArticleForm().getRecruitsNumbers();
        if (recruitmentArticle.getAttend() >= recruitsNumbers) {
            recruitmentArticle.setDeadLine(true);
            return RsData.of("F-1", "이미 참가 인원이 꽉 찼습니다.");
        }

        recruitmentPeople.setAttend(true);
        publisher.publishEvent(new EventAfterRecruitmentAttend(this, recruitmentPeople));

        return RsData.of("S-1", "수락 완료");
    }

    @Transactional
    public RsData deport(RecruitmentPeople recruitmentPeople) {
        recruitmentPeopleRepository.delete(recruitmentPeople);
        recruitmentPeople.getRecruitmentArticle().setDeadLine(false);
        publisher.publishEvent(new EventAfterDeportPeople(this, recruitmentPeople));
        return RsData.of("S-1", "추방 완료");
    }

    public RecruitmentPeople findById(Long id) {
        return recruitmentPeopleRepository.findById(id).get();
    }

    @Transactional
    public RsData checkedRealParticipant(RecruitmentPeople recruitmentPeople, boolean checkedReal) {
        recruitmentPeople.checkedParticipant(checkedReal);

        return RsData.of("S-1", "실제 참여자로 체크했습니다.");
    }

    public RsData<RecruitmentPeople> findByRecruitmentArticleAndMember(RecruitmentArticle recruitmentArticle,
                                                                       Member reviewer) {
        RecruitmentPeople recruitmentPeople = recruitmentPeopleRepository.findByRecruitmentArticleAndMember(
                recruitmentArticle, reviewer);

        if (recruitmentPeople != null) {
            return RsData.of("S-1", "신청이 확인되었습니다.", recruitmentPeople);
        }

        return RsData.of("F-1", "확인 안됐습니다.");
    }

    public List<RecruitmentPeople> findByMemberOrderByIdDesc(Member actor) {
        return recruitmentPeopleRepository.findByMemberOrderByIdDesc(actor);
    }
}
