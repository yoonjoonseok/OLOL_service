package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
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
    private final RecruitmentPeopleService recruitmentPeopleService;
    private int limitPeople = 0;

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend/delete")
    public String deleteAttend(@PathVariable Long id) {
        RecruitmentPeople one = recruitmentPeopleService.findOne(id);
        recruitmentPeopleService.delete(one);
        return "redirect:/recruitment/fromList";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend/create")
    public String createAttend(@PathVariable Long id) {
        RecruitmentPeople person = recruitmentPeopleService.findOne(id);
        RecruitmentArticle recruitmentArticle = person.getRecruitmentArticle();
        Long recruitsNumbers = recruitmentArticle.getRecruitmentArticleForm().getRecruitsNumbers();
        if (limitPeople >= recruitsNumbers) {
            return rq.historyBack("이미 참가 인원이 꽉 찼습니다.");
        }

        person.setAttend(true);

        recruitmentPeopleService.attend(person);
        limitPeople++;
        return "redirect:/recruitment/fromList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/attend")
    public String attendForm(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        List<RecruitmentPeople> recruitmentPeople = recruitmentArticle.get().getRecruitmentPeople();
        //현재 로그인한 회원에 아이디
        Long memberId = rq.getMember().getId();
        //게시글을 쓴 사람에 아이디
        Long articleMemberId = recruitmentArticle.get().getMember().getId();

        for (RecruitmentPeople people : recruitmentPeople) {
            if (people.getMember().getId() == memberId) {
                return rq.historyBack("이미 신청된 공고입니다.");
            }
        }

        if (articleMemberId == memberId) {
            return rq.historyBack("게시글을 작성한 사람은 참가를 누를 수 없습니다.");
        }

        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        return "usr/recruitment/attendForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/attend")
    public String attend(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        recruitmentPeopleService.saveRecruitmentPeople(rq.getMember().getId(), id);

        return "redirect:/recruitment/" + id;
    }
}
