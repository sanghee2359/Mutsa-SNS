package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;



    // post 업데이트
    @Transactional
    public int updateById(int postId, PostUpdateRequest request, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post updatePost = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        if(updatePost.getUser().getId() != user.getId()) throw new AppException(ErrorCode.INVALID_PERMISSION);

        // request title,body 주입
        updatePost.modify(request.toEntity());
        log.info("update Title:{}",updatePost.getTitle());
        return updatePost.getId();
    }

    // postId로 post 삭제
    public int deleteById(int postId, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        if(post.getUser().getId() != user.getId()) throw new AppException(ErrorCode.INVALID_PERMISSION);
        postRepository.delete(post);
        return post.getId();
    }



        // list 조회
    public Page<PostReadResponse> findAllPost() {

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        return postRepository.findAll(pageRequest).map(PostReadResponse::fromEntity);
    }


    // postId로 조회
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
        Post savedPost = postRepository.save(request.toEntity(user));
        log.info("postId:{}",savedPost.getId());
        log.info("userName:{}",user.getUsername());
        return savedPost.toPostDto();
    }

    public Page<PostReadResponse> findMyFeed(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        return postRepository.findAllByUser(user, pageRequest).map(PostReadResponse::fromEntity);
    }
}
