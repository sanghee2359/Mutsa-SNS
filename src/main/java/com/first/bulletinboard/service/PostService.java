package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.dto.post.PostReadListResponse;
import com.first.bulletinboard.domain.dto.post.PostReadOneResponse;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // id로 post 삭제
    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }

    // post 업데이트

    // list 조회
    public List<PostReadListResponse> getPostList(Pageable pageable) {
        Page<Post> posts =  postRepository.findAll(pageable);
        return posts.stream()
                .map(post -> PostReadListResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .body(post.getBody())
                        .userName(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt())
                        .lastModifiedAt(post.getLastModifiedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    // id 검색
    public Post getPost(int id) {
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
        return savedPost.toPostDto();
    }
}
