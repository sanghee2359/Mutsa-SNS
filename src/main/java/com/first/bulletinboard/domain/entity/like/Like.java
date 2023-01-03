package com.first.bulletinboard.domain.entity.like;

import com.first.bulletinboard.domain.entity.BaseEntity;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
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

    public Like(User user, Post post) { // 생성자
        this.user = user;
        this.post = post;
    }
}
