package com.first.bulletinboard.domain.entity.like;

import com.first.bulletinboard.domain.entity.BaseEntity;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@SQLDelete(sql="UPDATE likes SET deleted_at = current_timestamp WHERE id=?") // like를 삭제하는 sql문이 실행되어도 Update문이 실행 -> DB에는 남아있음.
//@Where(clause = "deleted_at is NULL") // deleted_at이
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Column(name = "deleted_at")
    private LocalDateTime DeletedAt;

    public Like(User user, Post post) { // 생성자
        this.user = user;
        this.post = post;
    }
}
