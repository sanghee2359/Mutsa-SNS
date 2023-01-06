package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.entity.user.UserRole;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // post 생성
    public PostDto createPost(PostCreateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post savedPost = postRepository.save(request.toEntity(user));
        return savedPost.toPostDto();
    }

    // list 조회
    public Page<PostDto> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return PostDto.toPostList(posts);
    }

    // postId로 단일 조회
    public PostDto findByPostId(int id) {
        Post post = findPostById(id);
        return post.toPostDto();
    }

    // my feed
    public Page<PostDto> findMyFeed(Pageable pageable, String userName) {
        User user = findUserByUserName(userName);
        Page<Post> posts= postRepository.findAllByUser(user, pageable);

        if(posts.isEmpty()) throw new AppException(ErrorCode.SUCCESS_GET_MYFEED);
        return PostDto.toPostList(posts);
    }

    // post 업데이트
    @Transactional
    public PostDto updateById(int postId, PostUpdateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post updatePost = findPostById(postId);

        if(isAccessible(updatePost, user)) updatePost.modify(request.toEntity());
        else throw new AppException(ErrorCode.INVALID_PERMISSION);
        return updatePost.toPostDto();
    }

    // postId로 post 삭제
    public PostDto deleteById(int postId, String userName) {
        User user = findUserByUserName(userName);
        Post deletePost = findPostById(postId);

        if(isAccessible(deletePost, user)) postRepository.delete(deletePost);
        else throw new AppException(ErrorCode.INVALID_PERMISSION);

        return deletePost.toPostDto();
    }


    /**
     * 접근 가능 조건
     * ADMIN or
     * post의 userId == user의 id
     */
    public boolean isAccessible(Post post, User user) {
        return (post.getUser().getId() == user.getId()) || (user.getRole() == UserRole.ADMIN);
    }

    /**
     * 중복 제거
     */
    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
    }
    public Post findPostById(int postId){
        return postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
    }

}
