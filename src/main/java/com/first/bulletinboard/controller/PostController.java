package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // post 삭제
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> deletePost(@PathVariable int id, Authentication authentication) {
        int deletePostId = postService.deleteById(id, authentication.getName());
        return Response.success(new PostDeleteResponse("포스트 삭제 완료",deletePostId));
    }
    // post 수정
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> updatePost(@PathVariable int id
            , @RequestBody PostUpdateRequest postUpdateRequest, Authentication authentication) {

        int updatePostId = postService.updateById(id, postUpdateRequest, authentication.getName());
        return Response.success(new PostUpdateResponse("포스트 수정 완료", updatePostId));
    }
    // post 등록
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication) {
        PostDto postDto = postService.create(postCreateRequest, authentication.getName());
        return Response.success(new PostCreateResponse("포스트 등록 완료",postDto.getId()));
    }

    // list 출력
    @GetMapping
    public Response<Page<PostReadResponse>> list(){
        Page<PostReadResponse> posts = postService.findAllPost();
        if(posts.isEmpty()) throw new AppException(ErrorCode.POST_NOT_FOUND);
        return Response.success(posts);
    }


    // postId로 post 상세 출력
    @GetMapping("/{id}")
    public Response<PostReadResponse> FindById(@PathVariable int id) {
        Post post = postService.findById(id);
        PostReadResponse response = PostReadResponse.fromEntity(post);
        return Response.success(response);
    }
}
