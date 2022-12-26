package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.dto.post.PostReadResponse;
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

    @ManyToOne(optional = false)
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

    /*public PostReadResponse toResponse() { // List -> user을 사용해서 그런지 registered가 없다고 뜸
        return PostReadResponse.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .userName(user.getUsername())
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }*/
}
