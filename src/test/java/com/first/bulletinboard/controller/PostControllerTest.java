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
    // 1. 테스트 블록 뚫어주기
    // 2. controller을 호출 했을 때 service를 호출했을 때 response값이 리턴되도록 -> service에 메소드를 만들어준다.
    // 3. 값도 넣어주어야 한다. request값
    // 4. controller에서 endpoint 블록 뚫어주기
    // 5. 또한 given, Mock객체에서 특정 메서드가 실행되는 경우 실제 return을 줄 수 없기 때문에 가정사항을 만든다.

    // given : 테스트를 위해 주어진 상태, 구체화하고자 하는 행동을 시작하기 전에 테스트 상태
    // when : 테스트 대상에게 가해진 어떠한 상태, 테스트 대상에게 주어진 어떠한 조건
    // then : 앞선 과정의 결과
//    private TestInfoFixture.TestInfo fixture =


    String token;
    @Value("${jwt.token.secret}")
    private String secretKey;
    @BeforeEach()
    public void token() {
        long expireTimeMs = 1000 * 60 * 60;
        token = JwtTokenUtil.createToken("user", secretKey, expireTimeMs);
    }
    User user = User.builder().id(1).userName("user").password("password").build();
    Post post1 = Post.builder().id(1).title("title1").body("body1").user(user).build();
    Post post2 = Post.builder().id(2).title("title2").body("body2").user(user).build();

    PostDto postDto1 = post1.toPostDto();
    PostDto postDto2 = post2.toPostDto();

    Comment comment1 = Comment.builder().id(1).comment("comment1").user(user).post(post1).build();
    Comment comment2 = Comment.builder().id(2).comment("comment2").user(user).post(post1).build();
    CommentDto commentDto1 = comment1.toDto();
    CommentDto commentDto2 = comment2.toDto();


    //-------------------------------------Post-------------------------------------//
    /**
     * create test
     */
    @Test
    @WithMockUser
    @DisplayName("[POST] post 작성 성공")
    void create_post_success() throws Exception {
        // given
        PostCreateRequest postRequest = new PostCreateRequest("title1", "body1");

        // when
        when(postService.createPost(any(), any()))
                .thenReturn(PostDto.builder().id(1).title("title1").body("body1").build());

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
    @DisplayName("[POST] 포스트 작성 실패(1) - 인증 실패 - JWT를 Bearer Token으로 보내지 않은 경우")
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
    @DisplayName("[POST] 포스트 작성 실패(2) - 인증 실패 - JWT가 유효하지 않은 경우")
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
    @DisplayName("[GET] postList 조회 성공 - 0번이 1번보다 날짜가 최신")
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
        // parameter을 캡쳐하는 클래스
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).findAllPost(pageableArgumentCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertEquals(20, pageRequest.getPageSize());
        assertEquals(Sort.by(DESC,"createdAt"), pageRequest.getSort());
    }

    @Test
    @WithMockUser
    @DisplayName("[GET] 포스트 1개 조회 성공 - id, title, body, userName 검증")
    void read_one_post_success() throws Exception {
        // given
        PostDto postDto = PostDto.builder()
                .id(1)
                .title("title")
                .body("body")
                .userName("userName")
                .build();

        // when
        when(postService.findByPostId(anyInt()))
                .thenReturn(postDto);
        // then
        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.title").value("title"))
                .andExpect(jsonPath("$.result.body").value("body"))
                .andExpect(jsonPath("$.result.userName").value("userName"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * update test
     */
    @Test
    @WithMockUser
    @DisplayName("[PUT] 포스트 수정 성공")
    void update_post_success() throws Exception {
        // given
        PostUpdateRequest request = PostUpdateRequest.builder().title("title").body("body").build();

        // when
        when(postService.updateById(anyInt(),any(),any())).thenReturn(PostDto.builder().id(1).build());

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
    @DisplayName("[PUT] 포스트 수정 실패(1) : 인증 실패")
    void update_post_fail1() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyInt(),any(),any()))
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
    @DisplayName("[PUT] 포스트 수정 실패(2) : 작성자 불일치")
    void update_post_fail2() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyInt(),any(),any()))
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
    @DisplayName("[PUT] 포스트 수정 실패(3) : 데이터베이스 에러")
    void update_post_fail3() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyInt(),any(),any()))
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
    @DisplayName("[DELETE] 포스트 삭제 성공")
    void delete_post_success() throws Exception {
        when(postService.deleteById(anyInt(),any())).thenReturn(PostDto.builder().id(1).build());

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
    @DisplayName("[DELETE] 포스트 삭제 실패(1) : 인증 실패")
    void delete_post_fail1() throws Exception {
        // given -> errorCode
        given(postService.deleteById(anyInt(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] 포스트 삭제 실패(2) : 작성자 불일치")
    void delete_post_fail2() throws Exception {
        // given -> errorCode
        given(postService.deleteById(anyInt(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] 포스트 삭제 실패(3) : 데이터베이스 에러")
    void delete_post_fail3() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest("title1", "body1");

        // given -> errorCode
        given(postService.updateById(anyInt(),any(),any()))
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
    @DisplayName("[GET] 마이피드 조회 성공")
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
    @DisplayName("[GET] 마이피드 조회 실패 - 인증 실패 - JWT가 유효하지 않은 경우")
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
    @DisplayName("[POST] comment 작성 성공")
    void create_comment_success() throws Exception {
        CommentDto dto = CommentDto.builder()
                .id(1)
                .comment("comment1")
                .userName("userName")
                .postId(1)
                .build();

        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyInt(),any(),any()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/v1/posts/{postId}/comments", 1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.userName").value("userName"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("[POST] 댓글 작성 실패(1) - 인증 실패 - JWT를 Bearer Token으로 보내지 않은 경우")
    void create_comment_fail1() throws Exception {
        CommentDto dto = CommentDto.builder()
                .id(1)
                .comment("comment1")
                .userName("userName")
                .postId(1)
                .build();

        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyInt(),any(),any()))
                .thenReturn(dto);

        // given -> errorCode
        given(postService.createComment(anyInt(), any(),any()))
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
    @DisplayName("[POST] 포스트 작성 실패(2) - 인증 실패 - JWT가 유효하지 않은 경우")
    void create_comment_fail2() throws Exception {
        CommentDto dto = CommentDto.builder()
                .id(1)
                .comment("comment1")
                .userName("userName")
                .postId(1)
                .build();

        CommentCreateRequest request = new CommentCreateRequest("comment1");
        when(postService.createComment(anyInt(),any(),any()))
                .thenReturn(dto);

        when(postService.createComment(anyInt(), any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ""));

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
    @DisplayName("[PUT] 댓글 수정 성공")
    void update_comment_success() throws Exception {
        // given
        CommentDto dto = CommentDto.builder()
                .id(1)
                .comment("updatedComment")
                .userName("userName")
                .postId(1)
                .build();
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("updatedComment").build();

        // when
        when(postService.updateComment(anyInt(),anyInt(),any(),any())).thenReturn(dto);

        // then
        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.comment").value("updatedComment"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("[PUT] 포스트 수정 실패(1) : 인증 실패")
    void update_comment_fail1() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("updatedComment").build();

        // given -> errorCode
        given(postService.updateComment(anyInt(),anyInt(),any(),any()))
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
    @DisplayName("[PUT] 포스트 수정 실패(1) : Post 없는 경우")
    void update_comment_fail2() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("updatedComment").build();

        // given -> errorCode
        given(postService.updateComment(anyInt(),anyInt(),any(),any()))
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
    @DisplayName("[PUT] 포스트 수정 실패(3) : 작성자 불일치")
    void update_comment_fail3() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("updatedComment").build();

        // given -> errorCode
        given(postService.updateComment(anyInt(),anyInt(),any(),any()))
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
    @DisplayName("[PUT] 포스트 수정 실패(4) : 데이터베이스 에러")
    void update_comment_fail4() throws Exception {
        CommentUpdateRequest request = CommentUpdateRequest.builder().comment("updatedComment").build();


        // given -> errorCode
        given(postService.updateComment(anyInt(),anyInt(),any(),any()))
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
    @DisplayName("[DELETE] 댓글 삭제 성공")
    void delete_comment_success() throws Exception {
        // given
        CommentDto dto = CommentDto.builder()
                .id(1)
                .comment("updatedComment")
                .userName("userName")
                .postId(1)
                .build();
        when(postService.deleteComment(anyInt(),anyInt(),any())).thenReturn(dto);

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
    @DisplayName("[DELETE] 댓글 삭제 실패(1) : 인증 실패")
    void delete_comment_fail1() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyInt(),anyInt(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] 댓글 삭제 실패(2) :  POST 없음")
    void delete_comment_fail2() throws Exception {
        given(postService.deleteComment(anyInt(),anyInt(),any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] 댓글 삭제 실패(3) : 작성자 불일치")
    void delete_comment_fail3() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyInt(),anyInt(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[DELETE] 댓글 삭제 실패(4) : 데이터베이스 에러")
    void delete_comment_fail4() throws Exception {
        // given -> errorCode
        given(postService.deleteComment(anyInt(),anyInt(),any()))
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
    @DisplayName("[GET] 댓글 목록 조회 성공")
    void read_commentList_success() throws Exception {
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto1);
        comments.add(commentDto2);
        PageRequest pageable = PageRequest.of(0,20,Sort.by("createdAt").descending());
        Page<CommentDto> commentList = new PageImpl<>(comments, pageable, 2);
        when(postService.findAllComments(anyInt(),any(Pageable.class))).thenReturn(commentList);

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
    @DisplayName("[POST] 좋아요 누르기 성공")
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
    @DisplayName("[POST] 좋아요 실패(1) : 인증 실패")
    void like_fail1() throws Exception {
        // given -> errorCode
        given(postService.pressLike(anyInt(),any()))
                .willThrow(new AppException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("[POST] 좋아요 실패(2) :  POST 없음")
    void like_fail2() throws Exception {
        given(postService.pressLike(anyInt(),any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}