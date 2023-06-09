package com.ll.olol.boundedContext.recruitment.repository;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<RecruitmentArticle, Long> {

    Page<RecruitmentArticle> findAll(Specification<RecruitmentArticle> spec, Pageable pageable);
    // 검색어의 조건을 만족하는 질문들(질문에는 답변, 작성자, 답변 작성자, 질문 제목, 질문 내용 등 다양한 요소를 만족하는)
    // 의 리스트를 spec에 저장하고, 그걸 pageable로 10개씩 표현하도록 한다. 정도?
}
