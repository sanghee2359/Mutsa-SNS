package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.comment.CommentDeleteResponse;
import com.first.bulletinboard.domain.dto.comment.CommentDto;
import com.first.bulletinboard.domain.dto.comment.CommentCreateRequest;
import com.first.bulletinboard.domain.dto.comment.CommentUpdateRequest;
import com.first.bulletinboard.domain.dto.post.PostReadResponse;
import com.first.bulletinboard.domain.dto.post.PostUpdateResponse;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public Response<CommentDto> createComment(@PathVariable Integer postId, @RequestBody CommentCreateRequest commentCreateRequest, Authentication authentication) {
        CommentDto commentDto = commentService.createComment(postId, commentCreateRequest, authentication.getName());
        return Response.success(commentDto);
    }
    @PutMapping("/{id}") // comment의 id
    public Response<CommentDto> updateComment(@PathVariable Integer postId, @PathVariable Integer id, @RequestBody CommentUpdateRequest commentUpdateRequest, Authentication authentication){
        CommentDto commentDto = commentService.updateComment(postId, id, commentUpdateRequest, authentication.getName());
        return Response.success(commentDto);
    }
    @DeleteMapping("/{id}")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable int postId, @PathVariable int id, Authentication authentication) {
        int deleteCommentId = commentService.deleteComment(postId, id, authentication.getName());
        return Response.success(new CommentDeleteResponse("댓글 삭제 완료", deleteCommentId));
    }
    @GetMapping
    public Response<Page<CommentDto>> list(@PathVariable int postId){
        Page<CommentDto> postComments = commentService.findAllComment(postId);
        if(postComments.isEmpty()) throw new AppException(ErrorCode.POST_NOT_FOUND);
        return Response.success(postComments);
    }
}
