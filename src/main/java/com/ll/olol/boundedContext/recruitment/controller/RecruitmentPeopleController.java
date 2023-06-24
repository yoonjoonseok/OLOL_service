package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RecruitmentPeopleController {
    private final Rq rq;
    private final MemberService memberService;
    private final RecruitmentService recruitmentService;
    private final RecruitmentPeopleService recruitmentPeopleService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/fromList")
    public String showFromAttendList(Model model) {

        Member loginedMember = rq.getMember();

        if (!memberService.hasAdditionalInfo(loginedMember)) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        Long memberId = rq.getMember().getId();
        List<RecruitmentArticle> all = recruitmentService.findAll();

        List<RecruitmentPeople> attendList = new ArrayList<>();
        List<RecruitmentArticle> myArticle = new ArrayList<>();

        //모든 게시물중에서
        //내가 쓴 게시물을 찾아서
        //내가 쓴 게시물에 신청자들을 다 뽑아온다.

        for (RecruitmentArticle recruitmentArticle : all) {
            if (recruitmentArticle.getMember().getId() == memberId) {
                myArticle.add(recruitmentArticle);

                List<RecruitmentPeople> recruitmentPeople = recruitmentArticle.getRecruitmentPeople();
                for (RecruitmentPeople recruitmentPeople1 : recruitmentPeople) {

                    attendList.add(recruitmentPeople1);

                }
            }
        }

        //내 게시글에 정보
        model.addAttribute("myArticle", myArticle);
        //내 글에 대한 신청자들 정보
        model.addAttribute("attendList", attendList);
        return "usr/member/fromAttendList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attendList")
    public String showAttendList(Model model) {
        Long memberId = rq.getMember().getId();
        Optional<Member> member = memberService.findById(memberId);
        List<RecruitmentPeople> recruitmentPeople = member.get().getRecruitmentPeople();

        model.addAttribute("peopleList", recruitmentPeople);
        return "usr/member/attendList";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend/delete")
    public String deleteAttend(@PathVariable Long id) {
        RecruitmentPeople one = recruitmentPeopleService.findOne(id);
        RsData<Object> rsData = recruitmentPeopleService.delete(one);
        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/fromList", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/attend/delete")
    public String deportPerson(@PathVariable Long id) {
        RecruitmentPeople one = recruitmentPeopleService.findOne(id);
        RsData<Object> rsData = recruitmentPeopleService.deport(one);
        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/fromList", rsData);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend/create")
    public String createAttend(@PathVariable Long id) {
        RecruitmentPeople person = recruitmentPeopleService.findOne(id);
        RsData rsData = recruitmentPeopleService.attend(person);

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/fromList", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend")
    public String attend(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        RsData rsData = recruitmentPeopleService.saveRecruitmentPeople(rq.getMember(), id);
        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/" + id, rsData);

    }
}
