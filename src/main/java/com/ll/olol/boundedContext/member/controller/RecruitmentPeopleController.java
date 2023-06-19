package com.ll.olol.boundedContext.member.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/fromList")
    public String showFromAttendList(Model model) {

        Member loginedMember = rq.getMember();

        if (!memberService.hasAdditionalInfo(loginedMember)) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        Long memberId = rq.getMember().getId();
        List<RecruitmentArticle> all = recruitmentService.findAll();
        List<RecruitmentPeople> list = new ArrayList<>();
        //모든 게시물중에서
        //내가 쓴 게시물을 찾아서
        //내가 쓴 게시물에 신청자들을 다 뽑아온다.

        for (RecruitmentArticle recruitmentArticle : all) {
            if (recruitmentArticle.getMember().getId() == memberId) {
                List<RecruitmentPeople> recruitmentPeople = recruitmentArticle.getRecruitmentPeople();
                for (RecruitmentPeople recruitmentPeople1 : recruitmentPeople) {
                    //신청자가 false일 경우만 추가
                    if (!recruitmentPeople1.isAttend()) {
                        list.add(recruitmentPeople1);
                    }
                }
            }
        }

        model.addAttribute("attendList", list);
        return "usr/recruitment/fromAttendList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attendList")
    public String showAttendList(Model model) {
        Long memberId = rq.getMember().getId();
        Optional<Member> member = memberService.findById(memberId);
        List<RecruitmentPeople> recruitmentPeople = member.get().getRecruitmentPeople();

        model.addAttribute("peopleList", recruitmentPeople);
        return "usr/recruitment/attendList";
    }
}
