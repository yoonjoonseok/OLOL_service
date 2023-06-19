package com.ll.olol.boundedContext.comment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class CommentController {

    private final Rq rq;
    private final CommentService commentService;

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
}