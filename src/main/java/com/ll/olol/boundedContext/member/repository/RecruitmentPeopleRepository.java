package com.ll.olol.boundedContext.member.repository;

import com.ll.olol.boundedContext.member.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentPeopleRepository extends JpaRepository<RecruitmentPeople, Long> {
    List<RecruitmentPeople> findAllByRecruitmentArticle(RecruitmentArticle recruitmentArticle);
}
