package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @AllArgsConstructor
    @Getter
    public static class CreateForm {
        //        @NotBlank
//        @Size(min = 3, max = 65)
        @NotBlank(message = "제목은 필수항목입니다.") // 아래의 변수는 비어있으면 안된다.
        @Size(max = 65, message = "제목을 200자 이하로 설정해주세요.") // 최대 200까지 가능하다.
        private String title;

        @NotBlank(message = "내용은 필수항목입니다.")
        @Size(max = 20000, message = "제목을 20000자 이하로 설정해주세요.")
        private String content;


        @NotNull(message = "타입은 필수항목입니다.")
        @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
        @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
        private Integer typeValue;

        @NotNull(message = "타입은 필수항목입니다.")
        @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
        @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
        private Integer dayNight;

        private String mountainName;

        @NotNull(message = "모집인원은 필수항목입니다.")
        @Min(value = 0, message = "올바른 인원수를 입력해주세요.")
        private Long recruitsNumber;

//        private LocalDateTime startTime;

//        private LocalDateTime courseTime;

        private Long ageRange;

        @NotBlank(message = "내용은 필수항목입니다.")
        private String connectType;

    }

    @GetMapping("/create")
    // @Valid를 붙여야 QuestionForm.java내의 NotBlank나 Size가 동작한다.
    public String questionCreate2(CreateForm createForm) {
        return "recruitmentArticle/createRecruitment_form";
    }

    @PostMapping("/create")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String questionCreate(@Valid CreateForm createForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recruitmentArticle/createRecruitment_form";
        }

        //recruitmentService.create(createForm.getTitle(), createForm.getContent(), /* member,  */createForm.getTypeValue());

        return "redirect:/"; // 질문 저장 후 질문목록으로 이동
    }


    @GetMapping("/{id}")
    public String showDetail() {
        return "usr/recruitment/detail";
    }
}
