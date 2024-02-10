package com.renew.sw.mentoring.domain.post.model.entity;

import com.renew.sw.mentoring.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String fileName;

    private String fileUrl;

    @Builder
    private PostImage(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void changePost(Post post) {
        if (this.post != null) {
            this.post.getImages().remove(this);
        }

        this.post = post;
        this.post.getImages().add(this);
    }
}