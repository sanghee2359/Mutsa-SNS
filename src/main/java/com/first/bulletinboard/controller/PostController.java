package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // list 출력
    /*@GetMapping("/posts")
    public Response<ResponseEntity<PostReadListResponse>> list(@RequestBody  Pageable pageable) {
        pageable = PageRequest.ofSize(20);
        List<PostReadListResponse> posts = postService.getPostList(pageable);
        // dto로 매핑하는 로직 -> controller에서 하기
//        VisitResponse visitResponse = Visit.toResponse();
        return Response.success(new ResponseEntity<>(posts));

    }*/

   /* <PostReadOneResponse>> list(@RequestBody Pageable pageable) {
        List<PostReadListResponse> posts = postService.getPostList(pageable);

        return Response.success(pageable);
    }*/
    //public Page<YourEntityHere> readPageable(@NotNull final Pageable pageable) {
    //    return someService.search(pageable);
    //}
/*public ResponseEntity<List<CommentDTO>> getComments(
           @RequestParam(defaultValue = "5", required = false)
                  Integer pageSize,
           @RequestParam(defaultValue = "0", required = false)
                  Integer page
    ) throws Exception {

        Pageable paging  = PageRequest.of(page, pageSize);

        List<CommentDTO> commentsDTO =
              commentService.getAllComments(paging);

        return new ResponseEntity<>(
              commentsDTO, HttpStatus.CREATED);
    }*/
    // id로 post 조회
    @GetMapping("/posts/{id}")
    public Response<PostReadOneResponse> FindById(@PathVariable int id) {
        Post post = postService.getPost(id);
        PostReadOneResponse reponse = new PostReadOneResponse(post.getId(),
                post.getTitle(),post.getBody(),post.getUser().getUsername(),
                post.getCreatedAt(), post.getLastModifiedAt());
        return Response.success(reponse);
    }
}
