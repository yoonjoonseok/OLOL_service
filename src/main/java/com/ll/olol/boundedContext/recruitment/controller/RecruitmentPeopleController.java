package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        if (memberService.additionalInfo(loginedMember).isFail()) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        List<RecruitmentArticle> myArticle = recruitmentService.findByMemberOrderByIdDesc(loginedMember);

        //내 게시글에 정보
        model.addAttribute("myArticle", myArticle);

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
        Member loginedMember = rq.getMember();
        if (rsData.isFail() || memberService.additionalInfo(loginedMember).isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/" + id, rsData);

    }
}
