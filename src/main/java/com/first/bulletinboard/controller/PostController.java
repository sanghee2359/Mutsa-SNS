package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // post 등록
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication) {
        PostDto dto = postService.createPost(postCreateRequest, authentication.getName());
        return Response.success(new PostCreateResponse("포스트 등록 완료",dto.getId()));
    }

    // list 출력
    @GetMapping
    public Response<Page<PostReadResponse>> findPostList(@PageableDefault(size = 20, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostDto> posts = postService.findAllPost(pageable);
        return Response.success(posts.map(PostReadResponse::of));
    }

    // postId로 post 상세 출력
    @GetMapping("/{id}")
    public Response<PostReadResponse> findPost(@PathVariable int id) {
        PostDto dto = postService.findByPostId(id);
        PostReadResponse response = PostReadResponse.of(dto);
        return Response.success(response);
    }
    // my feed -> 유저의 피드 목록 필터링
    @GetMapping("/my")
    public Response<Page<PostReadResponse>> myFeed (@PageableDefault(size = 20, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        Page<PostDto> posts = postService.findMyFeed(pageable, authentication.getName());
        return Response.success(posts.map(PostReadResponse::of));
    }
    // post 수정
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> updatePost(@PathVariable int id
            , @RequestBody PostUpdateRequest postUpdateRequest, Authentication authentication) {

        PostDto dto = postService.updateById(id, postUpdateRequest, authentication.getName());
        return Response.success(new PostUpdateResponse("포스트 수정 완료", dto.getId()));
    }
    // post 삭제
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> deletePost(@PathVariable int id, Authentication authentication) {
        PostDto dto = postService.deleteById(id, authentication.getName());
        return Response.success(new PostDeleteResponse("포스트 삭제 완료",dto.getId()));
    }

}
