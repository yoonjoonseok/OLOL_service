package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentFormRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    private final RecruitmentFormRepository recruitmentFormRepository;

    public Optional<RecruitmentArticle> findById(Long id) {
        return recruitmentRepository.findById(id);
    }


    public RecruitmentArticle createArticle(String articleName, String content, Integer typeValue) {
        RecruitmentArticle recruitmentArticle = new RecruitmentArticle();
        recruitmentArticle.setMember(null);
        recruitmentArticle.setArticleName(articleName);
        recruitmentArticle.setContent(content);
        recruitmentArticle.setViews(0L);
        recruitmentArticle.setTypeValue(typeValue);
        recruitmentArticle.setDeadLineDate(null);

        recruitmentRepository.save(recruitmentArticle);
        return recruitmentArticle;
    }

    public void createArticleForm(RecruitmentArticle recruitmentArticle, Integer dayNight, Long recruitsNumber,
                                  String mountainName, Long ageRange, String connectType, LocalDateTime startTime,
                                  LocalDateTime courseTime) {
        RecruitmentArticleForm recruitmentArticleForm = new RecruitmentArticleForm();

        recruitmentArticleForm.setRecruitmentArticle(recruitmentArticle);
        recruitmentArticleForm.setDayNight(dayNight);
        recruitmentArticleForm.setRecruitsNumbers(recruitsNumber);
        recruitmentArticleForm.setMountainName(mountainName);
        recruitmentArticleForm.setAgeRange(ageRange);
        recruitmentArticleForm.setConnectType(connectType);
        recruitmentArticleForm.setStartTime(startTime);
        recruitmentArticleForm.setCourseTime(courseTime);

        recruitmentFormRepository.save(recruitmentArticleForm);
    }

    public void updateArticleForm(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.save(recruitmentArticle);
    }

    public List<RecruitmentArticle> findAll() {
        return recruitmentRepository.findAll();
    }
}
