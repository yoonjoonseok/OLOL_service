package com.ll.olol.boundedContext.member.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.LikeableRecruitmentArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikeableRecruitmentArticleService likeableRecruitmentArticleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMypage(Model model) {
        Member actor = rq.getMember();
        model.addAttribute("member", actor);

        Optional<Member> member = memberRepository.findById(actor.getId());
        List<RecruitmentPeople> recruitmentPeople = member.get().getRecruitmentPeople();
        model.addAttribute("peopleList", recruitmentPeople);

        List<LikeableRecruitmentArticle> likeableRecruitmentArticles = likeableRecruitmentArticleService.findByFromMember(
                actor);
        Collections.reverse(likeableRecruitmentArticles);
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
        RsData result = memberService.modifyMemberInfo(rq.getMember(), editForm.getNickname(), editForm.getAgeRange(),
                editForm.getGender(), editForm.getEmail());
        if (result.isFail()) {
            return "redirect:/mypage";
        }
//        memberService.modifyUser(rq.getMember());
        return rq.redirectWithMsg("/recruitment/list", result.getMsg());
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login") // 로그인 폼, 로그인 폼 처리는 스프링 시큐리티가 구현, 폼 처리시에 CustomUserDetailsService 가 사용됨
    public String showLogin() {
        return "usr/member/login";
    }
}
