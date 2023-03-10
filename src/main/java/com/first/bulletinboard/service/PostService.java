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

    // post ์์ฑ
    public PostDto createPost(PostCreateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post savedPost = postRepository.save(request.toEntity(user));
        return savedPost.toPostDto();
    }

    // list ์กฐํ
    public Page<PostDto> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return PostDto.toPostList(posts);
    }

    // postId๋ก ๋จ์ผ ์กฐํ
    public PostDto findByPostId(Long id) {
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

    // post ์๋ฐ์ดํธ
    @Transactional
    public PostDto updateById(Long postId, PostUpdateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post updatePost = findPostById(postId);

        if(isAccessible(updatePost, user)) updatePost.modify(request.toEntity());
        else throw new AppException(ErrorCode.INVALID_PERMISSION);
        return updatePost.toPostDto();
    }

    // postId๋ก post ์ญ์?
    public PostDto deleteById(Long postId, String userName) {
        User user = findUserByUserName(userName);
        Post deletePost = findPostById(postId);

        if(isAccessible(deletePost, user)) {
            postRepository.delete(deletePost);
        }
        else throw new AppException(ErrorCode.INVALID_PERMISSION);

        return deletePost.toPostDto();
    }

    /**
     * ์?๊ทผ ๊ฐ๋ฅ ์กฐ๊ฑด
     * ADMIN or
     * post์ userId == user์ id
     */
    public boolean isAccessible(Post post, User user) {
        return (post.getUser().getId() == user.getId()) || (user.getRole() == UserRole.ADMIN);
    }


    //-------------------------------------Comment-------------------------------------//
    // create
    public CommentDto createComment(Long postId, CommentCreateRequest request, String userName) {
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);

        Comment savedComment = commentRepository.save(request.toEntity(user, post));
        alarmService.createCommentAlarm(savedComment); // comment ์๋ ์์ฑ
        return savedComment.toDto();
    }

    // comment update -> error๋ฉ์ธ์ง
    @Transactional
    public CommentDto updateComment(Long postId, Long id, CommentUpdateRequest request, String userName){
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);
        Comment comment = findCommentById(id);

        if(!isAccessible(comment, user))
            throw new AppException(ErrorCode.INVALID_PERMISSION);

        // ์์? : request comment ์ฃผ์
        comment.modify(request.toEntity(user, post));
        return comment.toDto();
    }
    public CommentDto deleteComment(Long postId, Long id, String userName) {
        User user = findUserByUserName(userName);
        findPostById(postId);

        Comment comment = findCommentById(id);

        if(!isAccessible(comment, user))
            throw new AppException(ErrorCode.INVALID_PERMISSION);

        // ์ญ์?
        commentRepository.delete(comment);
        return comment.toDto();
    }

    public Page<CommentDto> findAllComments(Long postId, Pageable pageable) {
        Post post = findPostById(postId);
        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        return CommentDto.toCommentList(comments);
    }
    /**
     * ์?๊ทผ ๊ฐ๋ฅ ์กฐ๊ฑด
     * ADMIN or
     * comment์ id == user์ id
     */
    public boolean isAccessible(Comment comment, User user) {
        return (comment.getUser().getId() == user.getId()) || (user.getRole() == UserRole.ADMIN);
    }


    //-------------------------------------Like-------------------------------------//


    // ์ข์์ ๋๋ฅด๊ธฐ
    public Like pressLike(Long postId, String userName) {
        User user = findUserByUserName(userName);
        Post post = findPostById(postId);

        if(isNotAlreadyPressed(user, post)) {
            Like like = likeRepository.save(new Like(user, post));
            alarmService.createLikeAlarm(like); // like ์๋ ์์ฑ
            return like;

        } else throw new AppException(ErrorCode.ALREADY_PRESSED_LIKE);
    }
    // ์ข์์ ๊ฐ์
    public Integer numberOfLikes(Long postId) {
        Post post = findPostById(postId);
        return post.getLikes().size();
    }
    // ์ข์์ ์ค๋ณต ํ์ธ
    public boolean isNotAlreadyPressed(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post).isEmpty();
    }


    /**
     * ์ค๋ณต ์?๊ฑฐ
     */
    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
    }
    public Post findPostById(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
    }
    public Comment findCommentById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
                });
    }

}
