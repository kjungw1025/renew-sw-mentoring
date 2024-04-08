package com.renew.sw.mentoring.domain.post.model.entity.dto.list;

import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.post.model.entity.dto.PostFileDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.PostImageDto;
import com.renew.sw.mentoring.infra.s3.service.AWSObjectStorageService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SummarizedGenericPostDto {

    @Schema(description = "게시글 아이디", example = "1")
    private final Long id;

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "작성자", example = "익명")
    private final String author;

    @Schema(description = "본문", example = "게시글 본문")
    private final String body;

    @Schema(description = "이미지 목록")
    private final List<PostImageDto> images;

    @Schema(description = "첨부파일 목록")
    private final List<PostFileDto> files;

    @Schema(description = "댓글 수", example = "16")
    private final int commentCount;

    @Schema(description = "게시글 생성 날짜", example = "2021-08-01T00:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "게시글 마지막 수정 날짜", example = "2021-08-01T00:00:00")
    private final LocalDateTime lastModifiedAt;

    public SummarizedGenericPostDto(AWSObjectStorageService s3service, int bodySize, Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getUser().getNickname();
        this.body = slice(post.getBody(), bodySize);
        this.images = PostImageDto.listOf(s3service, post.getImages());
        this.files = PostFileDto.listOf(s3service, post.getFiles());
        this.commentCount = post.getComments().size();
        this.createdAt = post.getCreatedAt();
        this.lastModifiedAt = post.getLastModifiedAt();
    }

    private static String slice(String text, int maxLen) {
        if (text == null) {
            return null;
        }
        return text.substring(0, Math.min(text.length(), maxLen));
    }
}
