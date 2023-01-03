package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{postId}/likes")
    public Response<?> pressLike(@PathVariable int postId, Authentication authentication) {
        likeService.pressLike(postId, authentication.getName());
        return Response.success("좋아요를 눌렀습니다.");
    }
    @GetMapping("/{postsId}/likes")
    public Response<?> numberOfLikes(@PathVariable int postsId) {
        int result = likeService.numberOfLikes(postsId);
        return Response.success(likeService.numberOfLikes(postsId));
    }
}
