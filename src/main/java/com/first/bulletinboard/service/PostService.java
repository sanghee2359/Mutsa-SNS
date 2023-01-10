package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.comment.CommentCreateRequest;
import com.first.bulletinboard.domain.dto.comment.CommentDto;
import com.first.bulletinboard.domain.dto.comment.CommentReadResponse;
import com.first.bulletinboard.domain.dto.comment.CommentUpdateRequest;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.entity.user.UserRole;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.CommentRepository;
import com.first.bulletinboard.repository.LikeRepository;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final AlarmService alarmService;
    //-------------------------------------Post-------------------------------------//

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
    public Page<PostDto> findMyFeed(String userName, Pageable pageable) {
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

        if(isAccessible(deletePost, user)) {
            postRepository.delete(deletePost);
        }
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


    //-------------------------------------Comment-------------------------------------//
    // create
    public CommentDto createComment(Integer postId, CommentCreateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);

        Comment savedComment = commentRepository.save(request.toEntity(user, post));
        alarmService.createCommentAlarm(savedComment); // comment 알람 생성
        return savedComment.toDto();
    }

    // comment update -> error메세지
    @Transactional
    public CommentDto updateComment(int postId, int id, CommentUpdateRequest request, String userName){
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);
        Comment comment = findCommentById(id);

        if(!isAccessible(comment, user))
            throw new AppException(ErrorCode.INVALID_PERMISSION);

        // 수정 : request comment 주입
        comment.modify(request.toEntity(user, post));
        return comment.toDto();
    }
    public CommentDto deleteComment(int postId, int id, String userName) {
        User user = findUserByUserName(userName);
        findPostById(postId);
        Comment comment = findCommentById(id);

        if(!isAccessible(comment, user))
            throw new AppException(ErrorCode.INVALID_PERMISSION);

        // 삭제
        commentRepository.delete(comment);
        return comment.toDto();
    }

    public Page<CommentDto> findAllComments(int postId, Pageable pageable) {
        Post post = findPostById(postId);
        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        return CommentDto.toCommentList(comments);
    }
    /**
     * 접근 가능 조건
     * ADMIN or
     * comment의 id == user의 id
     */
    public boolean isAccessible(Comment comment, User user) {
        return (comment.getUser().getId() == user.getId()) || (user.getRole() == UserRole.ADMIN);
    }


    //-------------------------------------Like-------------------------------------//


    // 좋아요 누르기
    public Like pressLike(int postId, String userName) {
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);

        if(isNotAlreadyPressed(user, post)) {
            Like like = likeRepository.save(new Like(user, post));
            alarmService.createLikeAlarm(like); // like 알람 생성
            return like;

        } else throw new AppException(ErrorCode.ALREADY_PRESSED_LIKE);
    }
    // 좋아요 개수
    public Integer numberOfLikes(int postId) {
        Post post = findPostById(postId);
        return post.getLikes().size();
    }
    // 좋아요 중복 확인
    public boolean isNotAlreadyPressed(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post).isEmpty();
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
    public Comment findCommentById(int id){
        return commentRepository.findById(id)
                .orElseThrow(()->{
                    throw new RuntimeException();
                });
    }

}
