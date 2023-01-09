package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.alarm.AlarmReadResponse;
import com.first.bulletinboard.domain.dto.comment.*;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.comment.Comment;
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

    //-------------------------------------Post-------------------------------------//

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
    public Response<Page<PostReadResponse>> myFeed (Authentication authentication, @PageableDefault(size = 20, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.findMyFeed(authentication.getName(), pageable);
        return Response.success(posts.map(PostReadResponse::of));
    }
    // post 수정
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> updatePost(Authentication authentication, @PathVariable int id
            , @RequestBody PostUpdateRequest postUpdateRequest) {

        PostDto dto = postService.updateById(id, postUpdateRequest, authentication.getName());
        return Response.success(new PostUpdateResponse("포스트 수정 완료", dto.getId()));
    }
    // post 삭제
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> deletePost(@PathVariable int id, Authentication authentication) {
        PostDto dto = postService.deleteById(id, authentication.getName());
        return Response.success(new PostDeleteResponse("포스트 삭제 완료",dto.getId()));
    }
    //-------------------------------------Comment-------------------------------------//


    @PostMapping
    public Response<CommentDto> createComment(@PathVariable Integer postId, @RequestBody CommentCreateRequest commentCreateRequest, Authentication authentication) {
        CommentDto commentDto = postService.createComment(postId, commentCreateRequest, authentication.getName());
        return Response.success(commentDto);
    }
    // read
    @GetMapping
    public Response<Page<CommentReadResponse>> findCommentList(@PathVariable int postId, @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comment = postService.findAllComments(postId, pageable);
        return Response.success(comment.map(CommentReadResponse::of));
    }
    // update
    @PutMapping("/{id}") // comment의 id
    public Response<CommentUpdateResponse> updateComment(@PathVariable Integer postId, @PathVariable Integer id, @RequestBody CommentUpdateRequest commentUpdateRequest, Authentication authentication){
        CommentDto commentDto = postService.updateComment(postId, id, commentUpdateRequest, authentication.getName());
        return Response.success(CommentUpdateResponse.of(commentDto));
    }
    // delete
    @DeleteMapping("/{id}")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable int postId, @PathVariable int id, Authentication authentication) {
        CommentDto commentDto = postService.deleteComment(postId, id, authentication.getName());
        return Response.success(new CommentDeleteResponse("댓글 삭제 완료", commentDto.getId()));
    }

    //-------------------------------------Like-------------------------------------//
    @PostMapping("/{postId}/likes")
    public Response<?> pressLike(@PathVariable int postId, Authentication authentication) {
        postService.pressLike(postId, authentication.getName());
        return Response.success("좋아요를 눌렀습니다.");
    }
    @GetMapping("/{postsId}/likes")
    public Response<?> numberOfLikes(@PathVariable int postsId) {
        return Response.success(postService.numberOfLikes(postsId));
    }

    //-------------------------------------Alarm-------------------------------------//
    @GetMapping
    public Response<Page<AlarmReadResponse>> findAlarmList(Authentication authentication, @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Alarm> dto = postService.findAllAlarms(authentication.getName(), pageable);
        return Response.success(dto.map(AlarmReadResponse::of));
    }
}
