package com.first.bulletinboard.service;

import com.first.bulletinboard.TestInfoFixture;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.repository.CommentRepository;
import com.first.bulletinboard.repository.LikeRepository;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// SpringBoot Dependency가 없이 Pojo만으로 테스트 가능 해야 합니다.
class PostServiceTest {
    /*PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    LikeRepository likeRepository = Mockito.mock(LikeRepository.class);
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    AlarmService alarmService = Mockito.mock(AlarmService.class);


    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository, likeRepository, commentRepository, alarmService);

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

        Assertions.assertDoesNotThrow(() -> postService.createPost(fixture.getTitle(), fixture.getBody(), fixture.getUserName()));
    }*/
}
