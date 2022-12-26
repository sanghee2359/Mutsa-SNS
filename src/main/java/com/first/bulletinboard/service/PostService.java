package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.dto.post.PostReadListResponse;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // postId로 post 삭제
    /*@Transactional
    public int deleteById(int postId, Authentication authentication) {
        postRepository.deleteById(postId);
    }*/

    // post 업데이트
    /*@Transactional
    public PostDto updateById(int postId, Authentication authentication) {

    }*/

    // list 조회
    public Page<Post> findAll(Pageable pageable) {
         return postRepository.findAll(pageable);
    }

    // postId로 검색
    public Post findById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        return post;
    }
    // post 생성
    public PostDto create(PostCreateRequest request, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post post = Post.builder()
                    .title(request.getTitle())
                    .body(request.getBody())
                    .user(user)
                    .build();

        Post savedPost = postRepository.save(post);
        log.info("postId:{}",post.getId());
        return savedPost.toPostDto();
    }
}
