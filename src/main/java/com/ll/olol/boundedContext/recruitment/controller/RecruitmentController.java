package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.CreateForm;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final Rq rq;
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;
    private final RecruitmentPeopleService recruitmentPeopleService;


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

        RecruitmentArticle recruitmentArticle = recruitmentService.createArticle(createForm.getArticleName(),
                createForm.getContent(), /* member,  */createForm.getTypeValue());
        recruitmentService.createArticleForm(recruitmentArticle, createForm.getDayNight(),
                createForm.getRecruitsNumber(), createForm.getMountainName(),
                createForm.getAgeRange(), createForm.getConnectType(), createForm.getStartTime(),
                createForm.getCourseTime());

        return "redirect:/";
    }

    @GetMapping("/{id}/attend")
    public String attendForm(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
//        //이미 마감 시간이 지났다면
//        System.out.println(recruitmentArticle.get().getDeadLineDate().isAfter(LocalDateTime.now()));
//        if (LocalDateTime.now().isAfter(recruitmentArticle.get().getDeadLineDate())) {
//            return rq.historyBack("마감 시간이 지났습니다.");
//        }

        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        return "usr/recruitment/attendForm";
    }

    @PostMapping("/{id}/attend")
    public String attend(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        Optional<RecruitmentArticle> article = recruitmentService.findById(id);
        recruitmentPeopleService.saveRecruitmentPeople(article.get().getMember().getId(), id);

        return "redirect:/";
    }

//    @PostMapping("/{id}/deadLine")
//    public String deadLineForm(@PathVariable Long id){
//
//    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        model.addAttribute("nowDate", LocalDateTime.now());
        return "usr/recruitment/detail";
    }
}
