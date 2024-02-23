package com.renew.sw.mentoring.domain.post.model.entity;

import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String fileId;

    private String contentType;

    private String fileName;


    @Builder
    private PostFile(String fileName, String fileId, String contentType) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.contentType = contentType;
    }

    public void changePost(Post post) {
        if (this.post != null) {
            this.post.getFiles().remove(this);
        }

        this.post = post;
        this.post.getFiles().add(this);
    }
}
