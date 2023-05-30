package com.ll.olol.boundedContext.comment.controller;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String createComment(@ModelAttribute Comment comment, Long id){

        return "redirect:/";
    }
}
