package com.first.bulletinboard.domain.entity.post;

import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.entity.BaseEntity;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.user.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted_at = current_timestamp WHERE post_id = ?")
@Where(clause = "deleted_at is NULL")
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


    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private Set<Like> likes = new HashSet<>();


    public PostDto toPostDto() {
        return PostDto.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .userName(this.user.getUsername())
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }
    public void modify(Post request) {
        this.title = request.getTitle();
        this.body = request.getBody();
    }
}
