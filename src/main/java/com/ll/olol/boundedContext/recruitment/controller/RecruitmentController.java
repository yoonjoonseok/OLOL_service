package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.recruitment.CreateForm;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final MemberRepository memberRepository;
    private final RecruitmentPeopleService recruitmentPeopleService;
    private int limitPeople = 0;

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
                createForm.getContent(), rq.getMember(), createForm.getTypeValue(), createForm.getDeadLineDate());
        recruitmentService.createArticleForm(recruitmentArticle, createForm.getDayNight(),
                createForm.getRecruitsNumber(), createForm.getMountainName(), createForm.getMtAddress(),
                createForm.getAgeRange(), createForm.getConnectType(), createForm.getStartTime(),
                createForm.getCourseTime());

        return "redirect:/";
    }

    @GetMapping("/list")
    public String showAttendList(Model model) {
        Long memberId = rq.getMember().getId();
        Optional<Member> member = memberRepository.findById(memberId);
        List<RecruitmentPeople> recruitmentPeople = member.get().getRecruitmentPeople();

        List<RecruitmentPeople> list = new ArrayList<>();
        //내가 신청한 목록들을 보여줍니다.

        for (RecruitmentPeople people : recruitmentPeople) {
            if (people.getMember().getId() == member.get().getId()) {
                list.add(people);
            }
        }
        model.addAttribute("peopleList", list);
        return "usr/recruitment/attendList";
    }

    @GetMapping("/fromList")
    public String showFromAttendList(Model model) {
        Long memberId = rq.getMember().getId();
        Optional<Member> member = memberRepository.findById(memberId);
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


    @PostMapping("/{id}/attend/delete")
    public String deleteAttend(@PathVariable Long id) {
        RecruitmentPeople one = recruitmentPeopleService.findOne(id);
        recruitmentPeopleService.delete(one);
        return "redirect:/recruitment/fromList";
    }

    @PostMapping("/{id}/attend/create")
    public String createAttend(@PathVariable Long id) {
        RecruitmentPeople person = recruitmentPeopleService.findOne(id);
        RecruitmentArticle recruitmentArticle = person.getRecruitmentArticle();
        Long recruitsNumbers = recruitmentArticle.getRecruitmentArticleForm().getRecruitsNumbers();
        if (limitPeople >= recruitsNumbers) {
            return rq.historyBack("이미 참가 인원이 꽉 찼습니다.");
        }

        person.setAttend(true);
        recruitmentPeopleService.update(person);
        limitPeople++;
        return "redirect:/recruitment/fromList";
    }

    @GetMapping("/{id}/attend")
    public String attendForm(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        Long recruitsNumbers = recruitmentArticle.get().getRecruitmentArticleForm().getRecruitsNumbers();
        //이제까지 신청한 인원들

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
//        if (recruitmentPeople.size() == recruitsNumbers) {
//            return rq.historyBack("이미 마감된 공고입니다.");
//        }
        if (articleMemberId == memberId) {
            return rq.historyBack("게시글을 작성한 사람은 참가를 누를 수 없습니다.");
        }

        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        return "usr/recruitment/attendForm";
    }

    @PostMapping("/{id}/attend")
    public String attend(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        Optional<RecruitmentArticle> article = recruitmentService.findById(id);

        recruitmentPeopleService.saveRecruitmentPeople(rq.getMember().getId(), id);

        return "redirect:/";
    }

    @PostMapping("/{id}/deadLine")
    public String deadLineForm(@PathVariable Long id, @ModelAttribute RecruitmentArticle recruitmentArticle) {
        Optional<RecruitmentArticle> article = recruitmentService.findById(id);
        if (rq.getMember().getId() != article.get().getMember().getId()) {
            return rq.historyBack("만든 사람만 마감버튼을 누를 수 있어요.");
        }
        article.get().setDeadLineDate(LocalDateTime.now());
        //마감 버튼을 누르면 마감 시간을 현재 시간으로 바꿈
        recruitmentService.updateArticleForm(article.get());
        return "redirect:/";
    }


    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        model.addAttribute("recruitmentArticle", recruitmentArticle.get());
        model.addAttribute("nowDate", LocalDateTime.now());
        return "usr/recruitment/detail";
    }


}
