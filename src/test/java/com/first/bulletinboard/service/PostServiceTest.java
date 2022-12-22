package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// SpringBoot Dependency가 없이 Pojo만으로 테스트 가능 해야 합니다.
class PostServiceTest {
 /*   PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
//    LikeEntityRepository likeEntityRepository = Mockito.mock(LikeEntityRepository.class);
//    CommentEntityRepository commentEntityRepository = Mockito.mock(CommentEntityRepository.class);


    @BeforeEach
    void setUp() {
//        postService = new PostService(postRepository, userRepository, likeEntityRepository, commentEntityRepository);
        postService = new PostService(postRepository, userRepository);

    }

    @Test
    @DisplayName("등록 성공")
    void post_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);

        Assertions.assertDoesNotThrow(() -> postService.write(fixture.getTitle(), fixture.getBody(), fixture.getUserName()));
    }*/
}