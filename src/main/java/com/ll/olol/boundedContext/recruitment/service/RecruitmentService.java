package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentFormRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    private final RecruitmentFormRepository recruitmentFormRepository;

    public Optional<RecruitmentArticle> findById(Long id) {
        return recruitmentRepository.findById(id);
    }


    public RecruitmentArticle createArticle(String articleName, String content, Member member, Integer typeValue, @NotNull(message = "마감일 지정은 필수항목입니다.") LocalDateTime deadLineDate) {
        RecruitmentArticle recruitmentArticle = new RecruitmentArticle();
        recruitmentArticle.setMember(member);
        recruitmentArticle.setArticleName(articleName);
        recruitmentArticle.setContent(content);
        recruitmentArticle.setViews(0L);
        recruitmentArticle.setTypeValue(typeValue);
        recruitmentArticle.setDeadLineDate(deadLineDate);

        recruitmentRepository.save(recruitmentArticle);
        return recruitmentArticle;
    }

    public void createArticleForm(RecruitmentArticle recruitmentArticle, Integer dayNight, Long recruitsNumber, String mountainName, String mtAddress, Long ageRange, String connectType, LocalDateTime startTime, LocalDateTime courseTime) {
        // 동만 붙은 부분만 가져옴
        RsData<String> checkMt = mtAddressChecked(mtAddress);

        String realMountainAddress = null;

        if (checkMt.isSuccess()) {
            realMountainAddress = checkMt.getData();
        }

        RecruitmentArticleForm recruitmentArticleForm = new RecruitmentArticleForm();

        recruitmentArticleForm.setRecruitmentArticle(recruitmentArticle);
        recruitmentArticleForm.setDayNight(dayNight);
        recruitmentArticleForm.setRecruitsNumbers(recruitsNumber);
        recruitmentArticleForm.setMountainName(mountainName);
        recruitmentArticleForm.setMtAddress(realMountainAddress);
        recruitmentArticleForm.setAgeRange(ageRange);
        recruitmentArticleForm.setConnectType(connectType);
        recruitmentArticleForm.setStartTime(startTime);
        recruitmentArticleForm.setCourseTime(courseTime);

        recruitmentFormRepository.save(recruitmentArticleForm);
    }


    private RsData<String> mtAddressChecked(String mountainName) {
        String[] mtPullAddress = mountainName.split(" ");

        String realMtAddress = null;

        for (String checkAdd : mtPullAddress) {
            if (checkAdd.endsWith("동")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("읍")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("면")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("가")) {
                realMtAddress = checkAdd;
            }
        }

        if (realMtAddress != null) return RsData.of("S-1", "주소를 저장했습니다.", realMtAddress);

        return RsData.of("F-1", "동을 저장 못함");
    }

    private Specification<RecruitmentArticle> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<RecruitmentArticle> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<RecruitmentArticle, Member> u1 = q.join("author", JoinType.LEFT);
                Join<RecruitmentArticle, Comment> a = q.join("answerList", JoinType.LEFT);
                Join<Comment, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Page<RecruitmentArticle> getlist(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        if (kw == null || kw.trim().length() == 0) {
            return recruitmentRepository.findAll(pageable);
        }
        Specification<RecruitmentArticle> spec = search(kw);
        return recruitmentRepository.findAll(spec, pageable);
    }

    public void updateArticleForm(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.save(recruitmentArticle);
    }
}
