package com.ll.olol.boundedContext.recruitment.repository;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentPeopleRepository extends JpaRepository<RecruitmentPeople, Long> {
    List<RecruitmentPeople> findAllByRecruitmentArticle(RecruitmentArticle recruitmentArticle);

    RecruitmentPeople findByRecruitmentArticleAndMember(RecruitmentArticle recruitmentArticle, Member reviewer);

    List<RecruitmentPeople> findByMemberOrderByIdDesc(Member actor);
}
