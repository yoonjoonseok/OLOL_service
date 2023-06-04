package com.ll.olol.boundedContext.member.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMyPage() {
        return "usr/layout/myPage";
    }

    @AllArgsConstructor
    @Getter
    public static class EditForm {
        @NotBlank
        @Size(min = 2, max = 10)
        private final String nickname;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public String editInfo(@Valid EditForm editForm) {
        RsData result = memberService.modifyMemberInfo(rq.getMember(), editForm.getNickname());

        if (result.isFail()) {
            rq.historyBack("다시시도해주세요");
        }

        return "redirect:/";
    }


}
