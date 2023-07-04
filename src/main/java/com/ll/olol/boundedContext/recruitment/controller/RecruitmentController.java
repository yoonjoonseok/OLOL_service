package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.entity.CreateForm;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.LikeableRecruitmentArticleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final Rq rq;
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final LikeableRecruitmentArticleService likeableRecruitmentArticleService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") Long ageRange,
                       @RequestParam(defaultValue = "0") int dayNight,
                       @RequestParam(defaultValue = "0") int typeValue,
                       @RequestParam(defaultValue = "1") int sortCode,
                       @RequestParam(defaultValue = "0") int page,
                       String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("isDeadLine"));
        if (sortCode == 1) {
            sorts.add(Sort.Order.desc("createDate"));
        }
        if (sortCode == 2) {
            sorts.add(Sort.Order.asc("createDate"));
        } else if (sortCode == 3) {
            sorts.add(Sort.Order.desc("views"));
        }

        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Page<RecruitmentArticle> paging = recruitmentService.getListByConditions(ageRange, dayNight, typeValue, kw,
                pageable);
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now);

        model.addAttribute("now", now);
        model.addAttribute("paging", paging);
        return "usr/recruitment/allList";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        if (recruitmentArticle.isEmpty()) {
            return rq.historyBack(RsData.of("F-1", "존재하지 않는 모임 공고입니다"));
        }

        recruitmentService.addView(recruitmentArticle.get());

        List<Comment> comments = commentService.findComments();
        List<Comment> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getRecruitmentArticle().getId() == id) {
                commentList.add(comment);
            }
        }

        model.addAttribute("commentForm", new CommentDto());
        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        model.addAttribute("comments", commentList);
        model.addAttribute("nowDate", LocalDateTime.now());
        model.addAttribute("writer", Long.toString(recruitmentArticle.get().getMember().getId()));
        if (rq.getMember() != null) {
            model.addAttribute("me", Long.toString(rq.getMember().getId()));
        }

        return "usr/recruitment/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    // @Valid를 붙여야 QuestionForm.java내의 NotBlank나 Size가 동작한다.
    public String questionCreate2(CreateForm createForm) {

        Member loginedMember = rq.getMember();
        if (!memberService.hasAdditionalInfo(loginedMember)) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        return "usr/recruitment/createRecruitment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String questionCreate(@Valid CreateForm createForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "usr/recruitment/createRecruitment_form";
        }

        RecruitmentArticle recruitmentArticle = recruitmentService.createArticle(createForm.getArticleName(),
                createForm.getContent(), rq.getMember(), createForm.getTypeValue(), createForm.getDeadLineDate());
        recruitmentService.createArticleForm(recruitmentArticle, createForm.getDayNight(),
                createForm.getRecruitsNumber(), createForm.getMountainName(), createForm.getMtAddress(),
                createForm.getAgeRange(), createForm.getConnectType(), createForm.getStartTime(),
                createForm.getStartTime().plusHours(createForm.getCourseTime()));
        RsData rsdata = RsData.of("S-1", "모임 글 작성 성공");
        return rq.redirectWithMsg("/recruitment/list", rsdata.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/update")
    // @Valid를 붙여야 QuestionForm.java내의 NotBlank나 Size가 동작한다.
    public String update(@PathVariable Long id, CreateForm createForm) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        RsData canUpdateRsData = recruitmentService.canUpdate(recruitmentArticle, rq.getMember());
        if (canUpdateRsData.isFail()) {
            return rq.historyBack(canUpdateRsData);
        }

        createForm.set(recruitmentArticle.get());

        return "usr/recruitment/createRecruitment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/update")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String update(@PathVariable Long id, @Valid CreateForm createForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "usr/recruitment/createRecruitment_form";
        }

        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        RsData canUpdateRsData = recruitmentService.canUpdate(recruitmentArticle, rq.getMember());
        if (canUpdateRsData.isFail()) {
            return rq.historyBack(canUpdateRsData);
        }

        RsData rsData = recruitmentService.update(recruitmentArticle.get(), createForm);

        recruitmentArticle.get().update(createForm);
        recruitmentArticle.get().getRecruitmentArticleForm().update(createForm);

        return rq.redirectWithMsg("/recruitment/" + id, rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        RsData canDeleteRsData = recruitmentService.canDelete(recruitmentArticle, rq.getMember());

        if (canDeleteRsData.isFail()) {
            return rq.historyBack(canDeleteRsData);
        }

        RsData rsData = recruitmentService.deleteArticle(recruitmentArticle.get());

        // admin 이면 신고된 게시글 삭제시 뒤로가기
        if (rq.getMember().isAdmin()) {
            return rq.historyBack(canDeleteRsData.getMsg());
        }

        return rq.redirectWithMsg("/recruitment/list", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/deadLine")
    public String deadLineForm(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        Optional<RecruitmentArticle> article = recruitmentService.findById(id);
        RsData rsData = recruitmentService.deadLine(rq.getMember(), article.get());

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }
        return rq.redirectWithMsg("/recruitment/" + id, rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/bookmark")
    public String add(@PathVariable Long id) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        Member actor = rq.getMember();

        RsData canAddRsData = likeableRecruitmentArticleService.canAdd(recruitmentArticle, actor);

        if (canAddRsData.isFail()) {
            return rq.historyBack(canAddRsData);
        }

        RsData rsData = likeableRecruitmentArticleService.add(recruitmentArticle.get(), actor);

        //return "redirect:/recruitment/" + id;
        return rq.redirectWithMsg("/member/mypage", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/bookmark")
    public String cancel(@PathVariable Long id) {
        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleService.findById(
                id);

        RsData canCancelRsData = likeableRecruitmentArticleService.canCancel(likeableRecruitmentArticle,
                rq.getMember());

        if (canCancelRsData.isFail()) {
            return rq.historyBack(canCancelRsData);
        }

        RsData rsData = likeableRecruitmentArticleService.cancel(likeableRecruitmentArticle.get());

        return rq.redirectWithMsg("/member/mypage", rsData);

    }
}
