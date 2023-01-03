package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.dto.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(this.id)
                .comment(this.comment)
                .userName(this.user.getUsername())
                .postId(this.post.getId())
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }
    public void modify(Comment comment){
        this.comment = comment.getComment();
    }
}
