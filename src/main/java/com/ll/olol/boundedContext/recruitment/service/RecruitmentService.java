package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentFormRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void updateArticleForm(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.save(recruitmentArticle);
    }
}
