package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // post 등록
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication) {
        PostDto postDto = postService.create(postCreateRequest, authentication.getName());
        return Response.success(new PostCreateResponse("포스트 등록 완료",postDto.getId()));
    }

    // list 출력
    /*@GetMapping("/posts")
    public Response<Page<Post>> list(Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);
        return Response.success(posts);

    }*/
    @GetMapping
    public Response<Page<PostReadResponse>> list(Pageable pageable){
        Page<PostReadResponse> posts = postService.findAllPost(pageable);
        if(posts.isEmpty()) throw new AppException(ErrorCode.POST_NOT_FOUND);
        return Response.success(posts);
    }


    // post id로 post 조회 -> error 수정
    @GetMapping("/{postId}")
    public Response<PostReadResponse> FindById(@PathVariable int postId) {
        Post post = postService.findById(postId);
        PostReadResponse response = PostReadResponse.fromEntity(post);
        return Response.success(response);
    }
}
