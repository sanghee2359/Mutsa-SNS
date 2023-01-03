package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.comment.CommentDto;
import com.first.bulletinboard.domain.dto.comment.CommentCreateRequest;
import com.first.bulletinboard.domain.dto.comment.CommentReadResponse;
import com.first.bulletinboard.domain.dto.comment.CommentUpdateRequest;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.CommentRepository;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // comment 생성
    public CommentDto createComment(Integer postId, CommentCreateRequest request, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });

        Comment savedComment = commentRepository.save(request.toEntity(user, post));
        return savedComment.toDto();
    }

    // comment update -> error메세지
    @Transactional
    public CommentDto updateComment(int postId, int id, CommentUpdateRequest request, String userName){
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    throw new RuntimeException();
                });

        if(comment.getUser().getId() != user.getId()) throw new AppException(ErrorCode.INVALID_PERMISSION);

        // request comment 주입
        comment.modify(request.toEntity(user, post));
        return comment.toDto();
    }
    public int deleteComment(int postId, int id, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    throw new RuntimeException();
                });
        if(comment.getUser().getId() != user.getId()) throw new AppException(ErrorCode.INVALID_PERMISSION);
        commentRepository.delete(comment);
        return comment.getId();
    }
    public Page<CommentDto> findAllComment(int postId) {
        postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return commentRepository.findAll(pageRequest).map(CommentReadResponse::fromEntity);
    }
}