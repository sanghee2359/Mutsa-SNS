package com.first.bulletinboard.domain.entity.like;

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
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@SQLDelete(sql = "UPDATE likes SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;


    public Like(User user, Post post) { // 생성자
        this.user = user;
        this.post = post;
    }


}
