package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.dto.post.PostDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;
    private String title;
    private String body;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id")
    private User user;


    public PostDto toPostDto() {
        return PostDto.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }

/*    public PostReadResponse toResponse() {
        return PostReadResponse.builder()
                .id(this.postId)
                .title(this.title)
                .body(this.body)
                .userName(user.getUsername())
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }*/
}
