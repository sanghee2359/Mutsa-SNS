package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.dto.post.PostUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;
    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Like> likes = new ArrayList<>();


    public PostDto toPostDto() {
        return PostDto.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }
    public void modify(Post request) {
        this.title = request.getTitle();
        this.body = request.getBody();
    }
}
