package com.renew.sw.mentoring.domain.comment.model.entity;

import com.renew.sw.mentoring.domain.comment.model.CommentStatus;
import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @Builder
    private Comment(@NotNull Post post,
                    @NotNull User user,
                    String content,
                    CommentStatus commentStatus) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.commentStatus = commentStatus;
    }
}
