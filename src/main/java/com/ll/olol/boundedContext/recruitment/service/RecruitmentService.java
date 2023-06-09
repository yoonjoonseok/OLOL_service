package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.api.localCode.LocalCodeApiClient;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentFormRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    private final RecruitmentFormRepository recruitmentFormRepository;

    private final LocalCodeApiClient localCodeApiClient;

    public Optional<RecruitmentArticle> findById(Long id) {
        return recruitmentRepository.findById(id);
    }


    public RecruitmentArticle createArticle(String articleName, String content, Member member, Integer typeValue,
                                            LocalDateTime deadLineDate) {
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

    public void createArticleForm(RecruitmentArticle recruitmentArticle, Integer dayNight, Long recruitsNumber,
                                  String mountainName, String mtAddress, Long ageRange, String connectType,
                                  LocalDateTime startTime, LocalDateTime courseTime) {
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

        recruitmentArticleForm.setLocalCode(localCodeApiClient.requestLocalCode(realMountainAddress));

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

        if (realMtAddress != null) {
            return RsData.of("S-1", "주소를 저장했습니다.", realMtAddress);
        }

        return RsData.of("F-1", "동을 저장 못함");
    }

    public void updateArticleForm(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.save(recruitmentArticle);
    }

    public void deleteArticle(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.delete(recruitmentArticle);
    }

    public RsData canDelete(Optional<RecruitmentArticle> recruitmentArticle, Member member) {
        if (recruitmentArticle.isEmpty())
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");
        if (recruitmentArticle.get().getMember().getId() != member.getId())
            return RsData.of("F-2", "모집자만이 삭제 가능합니다");
        return RsData.of("S-1", "모임 공고 삭제 가능");
    }

    public List<RecruitmentArticle> findAll() {
        return recruitmentRepository.findAll();
    }

    public void addView(RecruitmentArticle recruitmentArticle) {
        recruitmentArticle.setViews(recruitmentArticle.getViews() + 1);
        recruitmentRepository.save(recruitmentArticle);
    }
}
