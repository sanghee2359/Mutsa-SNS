package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.dto.user.UserDto;
import com.first.bulletinboard.domain.dto.user.UserJoinRequest;
import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.repository.LikeRepository;
import com.first.bulletinboard.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@ActiveProfiles("test") // local db에 row가 있는지 직접 확인 하고 싶을 때 씁니다.
class PostServiceWithSpringTest {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    LikeRepository likeRepository;

    @BeforeEach
    void setUp() {
    }
    UserJoinRequest joinRequest = new UserJoinRequest("sanghee","1234");
    PostCreateRequest postCreateRequest = new PostCreateRequest("title","body");


    @Test
    @DisplayName("soft delete test")
    void softDeleteWithLikeDelete() {

        // user등록
        UserDto userDto = userService.join(joinRequest);

        // post등록
        PostDto postDto = postService.createPost(postCreateRequest , userDto.getUserName());

        // like누름
        Like likeEntity = postService.pressLike(postDto.getId(), userDto.getUserName());

        // like조회
        Like selectedLike = likeRepository.findById(likeEntity.getId())
                .orElseThrow(() -> new RuntimeException("like가 없습니다."));

        // post삭제
        postService.deleteById(postDto.getId(),userDto.getUserName());

        // post선택
        Optional<Post> selectedPostEntity = postRepository.findById(postDto.getId());

        // post는 지워지고 없음
        assertTrue(selectedPostEntity.isEmpty());

        // like의 deletedAt이 updated되었는지 확인
        assertThrows(RuntimeException.class, ()->{
            Like foundLikeEntity = likeRepository.findById(likeEntity.getId())
                    .orElseThrow(()->new RuntimeException("해당 id의 like가 없습니다. id:"+likeEntity.getId()));
        });
    }
}

