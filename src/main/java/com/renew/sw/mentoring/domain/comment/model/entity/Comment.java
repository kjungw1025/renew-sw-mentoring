package com.renew.sw.mentoring.domain.comment.model.entity;

import com.renew.sw.mentoring.domain.comment.model.CommentStatus;
import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
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

    @Setter
    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL, fetch = LAZY)
    private List<Comment> childComments = new ArrayList<>();

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @Builder
    private Comment(@NotNull Post post,
                    @NotNull User user,
                    String content,
                    CommentStatus commentStatus,
                    Long parentCommentId) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.commentStatus = commentStatus;
        this.parentCommentId = parentCommentId;
    }


    public void addChildComment(Comment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    public void changePost(Post post) {
        if (this.post != null) {
            this.post.getComments().remove(this);
        }

        this.post = post;
        this.post.getComments().add(this);
    }

    public void updateContent(String content) {
        this.content = content;
        this.commentStatus = CommentStatus.EDITED;
    }

    public void markedAsDeleted(boolean isAdmin) {
        if(isAdmin) {
            this.commentStatus = CommentStatus.DELETED_BY_ADMIN;
        } else {
            this.commentStatus = CommentStatus.DELETED;
        }
    }
}
