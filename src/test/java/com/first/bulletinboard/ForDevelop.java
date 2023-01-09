package com.first.bulletinboard;

import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.exception.repository.LikeRepository;
import com.first.bulletinboard.exception.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ForDevelop {
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("soft delete 테스트")
    void postSaveLikeDeletePost() {
        Post postEntity = Post.builder()
                .title("eee")
                .body("fff")
                .build();
        // 글쓰기
        Post savedPost = postRepository.save(postEntity);

        Like likeEntity = Like.builder()
                .post(savedPost)
                .build();
        // 좋아요 누르기
        Like savedLike = likeRepository.save(likeEntity);
        System.out.println(savedLike.getPost().getId());
        System.out.println(savedLike.getId());

        // like를 누르고 개수 세기
        System.out.printf("like누르고 개수 세기:%d\n", likeRepository.count());

        // 좋아요 지우기
        likeRepository.deleteAllByPost(savedPost);
        System.out.printf("지우고 개수 세기:%d\n", likeRepository.count());
        Optional<Like> likeEntity1 = likeRepository.findById(savedLike.getId());
        System.out.println(likeEntity1.isEmpty());
        System.out.printf("deleted_at:%s\n", likeEntity1.get().getDeletedAt());

        // 글 지우기
        postRepository.delete(savedPost);

    }
}