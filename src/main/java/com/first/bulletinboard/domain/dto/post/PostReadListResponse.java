package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostReadListResponse {
    private Page<Post> posts;
    /*public static List<PostReadResponse> fromEntity(Post post) {
        List<PostReadResponse> postList = new ArrayList<>();
        for (Post x : post.getUser().getPosts()) {
            postList.add(PostReadResponse.fromEntity(x));
        }
        return postList;
    }*/
}
