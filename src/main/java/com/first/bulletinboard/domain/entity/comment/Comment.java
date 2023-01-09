package com.first.bulletinboard.domain.entity.comment;

import com.first.bulletinboard.domain.dto.comment.CommentDto;
import com.first.bulletinboard.domain.entity.BaseEntity;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
@SQLDelete(sql="UPDATE comment SET deleted_at = current_timestamp WHERE id=?") // like를 삭제하는 sql문이 실행되어도 Update문이 실행 -> DB에는 남아있음.
@Where(clause = "deleted_at is NULL") // deleted_at이 null인 경우만 가져오기
public class Comment extends BaseEntity {
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
