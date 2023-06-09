package com.ll.olol.boundedContext.member.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.LikeableRecruitmentArticleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final RecruitmentPeopleService recruitmentPeopleService;
    private final LikeableRecruitmentArticleService likeableRecruitmentArticleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMypage(Model model) {
        Member actor = rq.getMember();
        model.addAttribute("member", actor);


        List<RecruitmentPeople> recruitmentPeople = recruitmentPeopleService.findByMemberOrderByIdDesc(actor);
        model.addAttribute("peopleList", recruitmentPeople);

        List<LikeableRecruitmentArticle> likeableRecruitmentArticles = likeableRecruitmentArticleService.findByFromMemberOrderByIdDesc(actor);
        model.addAttribute("likeableRecruitmentArticles", likeableRecruitmentArticles);
        return "usr/layout/myPage";
    }


    @AllArgsConstructor
    @Getter
    public static class EditForm {
        @NotBlank
        @Size(min = 2, max = 10)
        private final String nickname;

        @NotBlank
        @Email
        @Size(min = 5, max = 40)
        private final String email;

        private final String gender;
        private final int ageRange;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public String editInfo(@Valid EditForm editForm) {
        RsData result = null;
        try {
            result = memberService.modifyMemberInfo(rq.getMember(), editForm.getNickname(), editForm.getAgeRange(), editForm.getGender(), editForm.getEmail());
        } catch (Exception e) {
            return rq.historyBack("중복된 닉네임 혹은 이메일입니다.");
        }

        return rq.redirectWithMsg("/recruitment/list", result.getMsg());
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login") // 로그인 폼, 로그인 폼 처리는 스프링 시큐리티가 구현, 폼 처리시에 CustomUserDetailsService 가 사용됨
    public String showLogin() {
        return "usr/member/login";
    }

    @GetMapping("/{id}/info")
    public String showInfo(@PathVariable Long id, Model model) {
        Optional<Member> member = memberService.findById(id);
        if (!member.isPresent()) {
            return rq.historyBack("회원 정보가 없습니다.");
        }

        model.addAttribute("member", member.get());

        return "usr/member/information";
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping("/mypage/notification")
    public String setNotificationOption(@RequestBody NotificationOption data) {
        memberService.setNotificationOption(rq.getMember(), data);

        return "success";
    }

    @Getter
    public static class NotificationOption {
        private boolean receivePush;
        private boolean receiveMail;
    }
}
