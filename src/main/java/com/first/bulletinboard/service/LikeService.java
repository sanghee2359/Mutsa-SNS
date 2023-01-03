package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.LikeRepository;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public boolean pressLike(int postId, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        if(isNotAlreadyPressed(user, post)) {
            likeRepository.save(new Like(user, post));
            return true;

        } else throw new AppException(ErrorCode.ALREADY_PRESSED_LIKE);
    }
    public boolean isNotAlreadyPressed(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post).isEmpty();
    }
    public Integer numberOfLikes(int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.POST_NOT_FOUND);
                });
        return post.getLikes().size();
    }
}
