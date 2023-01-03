package com.first.bulletinboard.repository;

import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByUser(User user, Pageable pageable);

}
