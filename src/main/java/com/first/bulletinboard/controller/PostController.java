package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostCreateResponse;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // post 등록
    @PostMapping("/posts")
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication) {
        PostDto postDto = postService.create(postCreateRequest, authentication.getName());

        return Response.success(new PostCreateResponse("포스트 등록 완료",postDto.getId()));
    }
}
