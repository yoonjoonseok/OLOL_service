package com.ll.olol.boundedContext.comment.controller;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller

@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @GetMapping("/comment")
    public String comment(Model model) {
        List<Comment> comments = commentService.findComments();
        if (!comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }
        return "usr/home/comment";
    }

    @PostMapping("/comment")
    public String createComment(@ModelAttribute Comment comment, String writer) {
        System.out.println(writer);
        comment.setCreateDate(LocalDateTime.now());
        commentService.commentSave(comment, writer);
        System.out.println("comment = " + comment.getContent());

        return "redirect:/comment";
    }

    @GetMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.commentDelete(commentId);
        return "redirect:/comment";
    }


    @GetMapping("/comment/{commentId}/edit")
    public String editCommentForm(@PathVariable("commentId") Long commentId, Model model) {
        Comment comment = commentService.findOne(commentId);

        model.addAttribute("comment", comment);
        return "usr/home/editComment";
    }

    @PostMapping("/comment/{commentId}/edit")
    public String editComment(@PathVariable("commentId") Long commentId, @ModelAttribute Comment comment) {
        commentService.update(commentId, comment.getContent());
        return "redirect:/comment";
    }
}