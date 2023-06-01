package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentRepository recruitmentRepository;

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentRepository.findById(id);
        RecruitmentArticleForm recruitmentArticleForm = null;

        if (recruitmentArticle.isPresent()) {
            recruitmentArticleForm = recruitmentArticle.get().getRecruitmentArticleForm();
        }

        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        model.addAttribute("recruitmentArticleForm", recruitmentArticleForm);
        return "usr/recruitment/detail";
    }
}
