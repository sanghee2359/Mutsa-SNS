package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.dto.comment.CommentCreateRequest;
import com.first.bulletinboard.domain.dto.comment.CommentDto;
import com.first.bulletinboard.domain.dto.comment.CommentUpdateRequest;
import com.first.bulletinboard.domain.dto.post.*;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.CommentRepository;
import com.first.bulletinboard.repository.PostRepository;
import com.first.bulletinboard.service.PostService;
import com.first.bulletinboard.service.UserService;
import com.first.bulletinboard.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @MockBean
    PostRepository postRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    // 1. ????????? ?????? ????????????
    // 2. controller??? ?????? ?????? ??? service??? ???????????? ??? response?????? ??????????????? -> service??? ???????????? ???????????????.
    // 3. ?????? ??????????????? ??????. request???
    // 4. controller?????? endpoint ?????? ????????????
    // 5. ?????? given, Mock???????????? ?????? ???????????? ???????????? ?????? ?????? return??? ??? ??? ?????? ????????? ??????????????? ?????????.

    // given : ???????????? ?????? ????????? ??????, ?????????????????? ?????? ????????? ???????????? ?????? ????????? ??????
    // when : ????????? ???????????? ????????? ????????? ??????, ????????? ???????????? ????????? ????????? ??????
    // then : ?????? ????????? ??????
//    private TestInfoFixture.TestInfo fixture =


    String token;
    @Value("${jwt.token.secret}")
    private String secretKey;
    @BeforeEach()
    public void token() {
        long expireTimeMs = 1000 * 60 * 60;
        token = JwtTokenUtil.generateToken(user, secretKey, expireTimeMs);
    }
    User user = User.builder().id(1L).userName("user").password("password").build();
    Post post1 = Post.builder().id(1L).title("title1").body("body1").user(user).build();
    Post post2 = Post.builder().id(2L).title("title2").body("body2").user(user).build();

    PostDto postDto1 = post1.toPostDto();
    PostDto postDto2 = post2.toPostDto();

    Comment comment1 = Comment.builder().id(1L).comment("comment1").user(user).post(post1).build();
    Comment comment2 = Comment.builder().id(2L).comment("comment2").user(user).post(post1).build();
    CommentDto commentDto1 = comment1.toDto();
    CommentDto commentDto2 = comment2.toDto();


    //-------------------------------------Post-------------------------------------//
    /**
     * create test
     */
    @Test
    @WithMockUser
    @DisplayName("[POST] post ?????? ??????")
    void create_post_success() throws Exception {
        // given
        PostCreateRequest postRequest = new PostCreateRequest("title1", "body1");

        // when
        when(postService.createPost(any(), any()))
                .thenReturn(PostDto.builder().id(1L).title("title1").body("body1").build());

        // then
        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ?????? ??????(1) - ?????? ?????? - JWT??? Bearer Token?????? ????????? ?????? ??????")
    void create_post_fail1() throws Exception {
        PostCreateRequest postRequest = new PostCreateRequest("title1", "body1");

        // given -> errorCode
        given(postService.createPost(any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        // then
        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ?????? ??????(2) - ?????? ?????? - JWT??? ???????????? ?????? ??????")
    void create_post_fail2() throws Exception {
        PostCreateRequest postRequest = new PostCreateRequest("title1", "body1");

        // given -> errorCode
        given(postService.createPost(any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        // then
        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /**
     * read test
     */
    @Test
    @WithMockUser
    @DisplayName("[GET] postList ?????? ?????? - 0?????? 1????????? ????????? ??????")
    void read_postList_success() throws Exception {
        // given
        postRepository.save(post1);
        postRepository.save(post2);
        List<PostDto> posts = new ArrayList<>();
        posts.add(postDto1);
        posts.add(postDto2);

        PageRequest pageable = PageRequest.of(0,20,Sort.by("createdAt").descending());
        Page<PostDto> postList = new PageImpl<>(posts, pageable, 2);
        when(postService.findAllPost(any(Pageable.class))).thenReturn(postList);

        // when
        mockMvc.perform(get("/api/v1/posts")
                        .with(csrf())
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        // parameter??? ???????????? ?????????
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).findAllPost(pageableArgumentCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertEquals(20, pageRequest.getPageSize());
        assertEquals(Sort.by(DESC,"createdAt"), pageRequest.getSort());
    }

    @Test
    @WithMockUser
    @DisplayName("[GET] ????????? 1??? ?????? ?????? - id, title, body, userName ??????")
    void read_one_post_success() throws Exception {
        // when
        when(postService.findByPostId(anyLong()))
                .thenReturn(postDto1);
        // then
        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.title").value("title1"))
                .andExpect(jsonPath("$.result.body").value("body1"))
                .andExpect(jsonPath("$.result.userName").value("user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * update test
     */
    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????")
    void update_post_success() throws Exception {
        // given
        PostUpdateRequest request = PostUpdateRequest.builder().title("title").body("body").build();

        // when
        when(postService.updateById(anyLong(),any(),any())).thenReturn(postDto1);

        // then
        mockMvc.perform(put("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(1) : ?????? ??????")
    void update_post_fail1() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        // then
        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(2) : ????????? ?????????")
    void update_post_fail2() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        // then
        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(3) : ?????????????????? ??????")
    void update_post_fail3() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR));

        // then
        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }


    /**
     * delete test
     */
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ????????? ?????? ??????")
    void delete_post_success() throws Exception {
        when(postService.deleteById(anyLong(),any())).thenReturn(postDto1);

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[DELETE] ????????? ?????? ??????(1) : ?????? ??????")
    void delete_post_fail1() throws Exception {
        // given -> errorCode
        given(postService.deleteById(anyLong(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ????????? ?????? ??????(2) : ????????? ?????????")
    void delete_post_fail2() throws Exception {
        // given -> errorCode
        given(postService.deleteById(anyLong(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ????????? ?????? ??????(3) : ?????????????????? ??????")
    void delete_post_fail3() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    /**
     * my feed test
     */

    @Test
    @WithMockUser
    @DisplayName("[GET] ???????????? ?????? ??????")
    void myfeed_success() throws Exception {
        postRepository.save(post1);
        postRepository.save(post2);
        List<PostDto> posts = new ArrayList<>();
        posts.add(postDto1);
        posts.add(postDto2);
        PageRequest pageable = PageRequest.of(0,20,Sort.by("createdAt").descending());
        Page<PostDto> postList = new PageImpl<>(posts, pageable, 2);
        when(postService.findMyFeed(any(),any(Pageable.class))).thenReturn(postList);

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andExpect(jsonPath("$.result.content[0].id").value(1))
                .andExpect(jsonPath("$.result.content[0].title").value("title1"))
                .andExpect(jsonPath("$.result.content[0].body").value("body1"))
                .andExpect(jsonPath("$.result.content[0].userName").value("user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[GET] ???????????? ?????? ?????? - ?????? ?????? - JWT??? ???????????? ?????? ??????")
    void myfeed_fail() throws Exception {
        given(postService.findMyFeed(any(),any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    //-------------------------------------Comment-------------------------------------//

    /**
     * create test
     */
    @Test
    @WithMockUser
    @DisplayName("[POST] comment ?????? ??????")
    void create_comment_success() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyLong(),any(),any()))
                .thenReturn(commentDto1);

        mockMvc.perform(post("/api/v1/posts/{postId}/comments", 1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.userName").value("user"))
                .andExpect(jsonPath("$.result.postId").value(1))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("[POST] ?????? ?????? ??????(1) - ?????? ?????? - JWT??? Bearer Token?????? ????????? ?????? ??????")
    void create_comment_fail1() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyLong(),any(),any()))
                .thenReturn(commentDto1);

        // given -> errorCode
        given(postService.createComment(anyLong(), any(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(post("/api/v1/posts/{postId}/comments",1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ?????? ??????(2) - ?????? ?????? - JWT??? ???????????? ?????? ??????")
    void create_comment_fail2() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyLong(),any(),any()))
                .thenReturn(commentDto1);

        when(postService.createComment(anyLong(), any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * update test
     */
    @Test
    @WithMockUser
    @DisplayName("[PUT] ?????? ?????? ??????")
    void update_comment_success() throws Exception {
        // given
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("comment1").build();

        // when
        when(postService.updateComment(anyLong(),anyLong(),any(),any())).thenReturn(commentDto1);

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.comment").value("comment1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("[PUT] ????????? ?????? ??????(1) : ?????? ??????")
    void update_comment_fail1() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("comment1").build();

        // given -> errorCode
        given(postService.updateComment(anyLong(),anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(1) : Post ?????? ??????")
    void update_comment_fail2() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("comment1").build();

        // given -> errorCode
        given(postService.updateComment(anyLong(),anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(3) : ????????? ?????????")
    void update_comment_fail3() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("comment1").build();

        // given -> errorCode
        given(postService.updateComment(anyLong(),anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    @DisplayName("[PUT] ????????? ?????? ??????(4) : ?????????????????? ??????")
    void update_comment_fail4() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("comment1").build();


        // given -> errorCode
        given(postService.updateComment(anyLong(),anyLong(),any(),any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR));

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    /**
     * delete test
     */

    @Test
    @WithMockUser
    @DisplayName("[DELETE] ?????? ?????? ??????")
    void delete_comment_success() throws Exception {
        when(postService.deleteComment(anyLong(),anyLong(),any())).thenReturn(commentDto1);

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[DELETE] ?????? ?????? ??????(1) : ?????? ??????")
    void delete_comment_fail1() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyLong(),anyLong(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ?????? ?????? ??????(2) :  POST ??????")
    void delete_comment_fail2() throws Exception {
        given(postService.deleteComment(anyLong(),anyLong(),any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ?????? ?????? ??????(3) : ????????? ?????????")
    void delete_comment_fail3() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyLong(),anyLong(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] ?????? ?????? ??????(4) : ?????????????????? ??????")
    void delete_comment_fail4() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyLong(),anyLong(),any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
    /**
     * read test
     */
    @Test // error
    @WithMockUser
    @DisplayName("[GET] ?????? ?????? ?????? ??????")
    void read_commentList_success() throws Exception {
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto1);
        comments.add(commentDto2);
        PageRequest pageable = PageRequest.of(0,20,Sort.by("createdAt").descending());
        Page<CommentDto> commentList = new PageImpl<>(comments, pageable, 2);
        when(postService.findAllComments(anyLong(),any(Pageable.class))).thenReturn(commentList);

        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .with(csrf())
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andExpect(jsonPath("$.result.content[0].id").value(1))
                .andExpect(jsonPath("$.result.content[0].comment").value("comment1"))
                .andExpect(jsonPath("$.result.content[0].userName").value("user"))
                .andExpect(jsonPath("$.result.content[0].postId").value(1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //-------------------------------------Like-------------------------------------//

    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ????????? ??????")
    void like_success() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result").exists())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ??????(1) : ?????? ??????")
    void like_fail1() throws Exception {
        // given -> errorCode
        given(postService.pressLike(anyLong(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[POST] ????????? ??????(2) :  POST ??????")
    void like_fail2() throws Exception {
        given(postService.pressLike(anyLong(),any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}