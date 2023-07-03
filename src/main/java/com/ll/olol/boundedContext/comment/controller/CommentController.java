package com.ll.olol.boundedContext.comment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final Rq rq;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public String createComment(@PathVariable("id") Long id,
                                @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/recruitment/" + id;
        }

        commentDto.setCreateDate(LocalDateTime.now());
        RsData rsData = commentService.commentSave(commentDto, id);
        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/recruitment/" + id, rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deleteComment(@PathVariable("id") Long id) {

        Comment comment = commentService.findOne(id);
        Long articleId = comment.getRecruitmentArticle().getId();
        RsData rsData1 = commentService.commentDelete(id);

        return rq.redirectWithMsg("/recruitment/" + articleId, rsData1);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String editCommentForm(@PathVariable("id") Long id, Model model) {
        Comment comment = commentService.findOne(id);

        model.addAttribute("comment", comment);
        return "usr/comment/editComment";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String editComment(@PathVariable("id") Long id, @ModelAttribute Comment comment) {
        RsData update = commentService.update(id, comment.getContent());
        if (update.isFail()) {
            return rq.historyBack(update);
        }
        Comment comment1 = commentService.findOne(id);
        Long articleId = comment1.getRecruitmentArticle().getId();

        return rq.redirectWithMsg("/recruitment/" + articleId, update);
    }
}