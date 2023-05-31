package com.ll.olol.boundedContext.comment.repository;

import com.ll.olol.boundedContext.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
