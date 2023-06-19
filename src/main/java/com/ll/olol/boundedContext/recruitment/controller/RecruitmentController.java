package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.CreateForm;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final Rq rq;
    private final RecruitmentService recruitmentService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CommentService commentService;
    private final RecruitmentPeopleService recruitmentPeopleService;
    private int limitPeople = 0;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    // @Valid를 붙여야 QuestionForm.java내의 NotBlank나 Size가 동작한다.
    public String questionCreate2(CreateForm createForm) {

        Member loginedMember = rq.getMember();
        if (!memberService.hasAdditionalInfo(loginedMember)) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        return "recruitmentArticle/createRecruitment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String questionCreate(@Valid CreateForm createForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recruitmentArticle/createRecruitment_form";
        }

        RecruitmentArticle recruitmentArticle = recruitmentService.createArticle(createForm.getArticleName(),
                createForm.getContent(), rq.getMember(), createForm.getTypeValue(), createForm.getDeadLineDate());
        recruitmentService.createArticleForm(recruitmentArticle, createForm.getDayNight(),
                createForm.getRecruitsNumber(), createForm.getMountainName(), createForm.getMtAddress(),
                createForm.getAgeRange(), createForm.getConnectType(), createForm.getStartTime(),
                createForm.getCourseTime());

        return "redirect:/";
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

        return "recruitmentArticle/updateRecruitment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/update")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String update(@PathVariable Long id, @Valid CreateForm createForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "recruitmentArticle/updateRecruitment_form";
        }

        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        RsData canUpdateRsData = recruitmentService.canUpdate(recruitmentArticle, rq.getMember());
        if (canUpdateRsData.isFail()) {
            return rq.historyBack(canUpdateRsData);
        }

        recruitmentService.update(recruitmentArticle.get(), createForm);

        recruitmentArticle.get().update(createForm);
        recruitmentArticle.get().getRecruitmentArticleForm().update(createForm);

        return "redirect:/recruitment/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attendList")
    public String showAttendList(Model model) {
        Long memberId = rq.getMember().getId();
        Optional<Member> member = memberRepository.findById(memberId);
        List<RecruitmentPeople> recruitmentPeople = member.get().getRecruitmentPeople();

        model.addAttribute("peopleList", recruitmentPeople);
        return "usr/recruitment/attendList";
    }

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/deadLine")
    public String deadLineForm(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        Optional<RecruitmentArticle> article = recruitmentService.findById(id);
        if (rq.getMember().getId() != article.get().getMember().getId()) {
            return rq.historyBack("만든 사람만 마감버튼을 누를 수 있어요.");
        }
        article.get().setDeadLineDate(LocalDateTime.now());
        //마감 버튼을 누르면 마감 시간을 현재 시간으로 바꿈
        recruitmentService.updateArticleForm(article.get());
        return "redirect:/recruitment/" + id;
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
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        RsData canDeleteRsData = recruitmentService.canDelete(recruitmentArticle, rq.getMember());

        if (canDeleteRsData.isFail()) {
            return rq.historyBack(canDeleteRsData);
        }

        recruitmentService.deleteArticle(recruitmentArticle.get());

        // admin 이면 신고된 게시글 삭제시 뒤로가기
        if (rq.getMember().isAdmin())
            return rq.historyBack(canDeleteRsData.getMsg());

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comment")
    public String createComment(@PathVariable("id") Long id,
                                @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/recruitment/" + id;
        }

        commentDto.setCreateDate(LocalDateTime.now());
        commentService.commentSave(commentDto, id);

        return "redirect:/recruitment/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable("id") Long id) {
        RsData rsData = commentService.isEqualMemberById(id);
        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }
        Comment comment = commentService.findOne(id);
        Long articleId = comment.getRecruitmentArticle().getId();
        commentService.commentDelete(id);
        return "redirect:/recruitment/" + articleId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/{id}/edit")
    public String editCommentForm(@PathVariable("id") Long id, Model model) {
        Comment comment = commentService.findOne(id);
        RsData rsData = commentService.isEqualMemberById(id);

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        model.addAttribute("comment", comment);
        return "usr/home/editComment";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/{id}/edit")
    public String editComment(@PathVariable("id") Long id, @ModelAttribute Comment comment) {
        commentService.update(id, comment.getContent());
        Comment comment1 = commentService.findOne(id);
        Long articleId = comment1.getRecruitmentArticle().getId();

        return "redirect:/recruitment/" + articleId;
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") Long ageRange,
                       @RequestParam(defaultValue = "0") int dayNight,
                       @RequestParam(defaultValue = "0") int typeValue,
                       @RequestParam(defaultValue = "1") int sortCode,
                       @RequestParam(defaultValue = "0") int page,
                       String kw) {
        List<Sort.Order> sorts = new ArrayList<>();

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
}
