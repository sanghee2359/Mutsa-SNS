package com.first.bulletinboard.repository;

import com.first.bulletinboard.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}