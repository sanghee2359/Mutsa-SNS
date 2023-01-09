package com.first.bulletinboard.exception.repository;

import com.first.bulletinboard.domain.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAll(Pageable pageable);
}
