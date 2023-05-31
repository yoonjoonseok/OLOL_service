package com.ll.olol.boundedContext.comment.controller;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller

@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @GetMapping("/comment")
    public String comment(Model model){
        List<Comment> comments = commentService.findComments();
        if(!comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }
        return "usr/home/comment";
    }

    @PostMapping("/comment")
    public String createComment(@ModelAttribute Comment comment,String writer){
        System.out.println(writer);
        comment.setCreateDate(LocalDateTime.now());
        commentService.commentSave(comment,writer);
        System.out.println("comment = " + comment.getContent());

        return "redirect:/comment";
    }

    @GetMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        commentService.commentDelete(commentId);
        return "redirect:/comment";
    }


    @GetMapping("/comment/{commentId}/edit")
    public String editCommentForm(@PathVariable("commentId") Long commentId, Model model){
        Comment comment = commentService.findOne(commentId);

        model.addAttribute("comment",comment);
        return "usr/home/editComment";
    }

    @PostMapping("/comment/{commentId}/edit")
    public String editComment(@PathVariable("commentId") Long commentId ,@ModelAttribute Comment comment){
        Comment comment1 = commentService.findOne(commentId);

        comment1.setContent(comment.getContent());
        commentService.update(comment1);

        return "redirect:/comment";


    }
}
