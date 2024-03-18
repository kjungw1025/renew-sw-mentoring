package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class RequestCreateGenericPostDto<T> {

    @NotBlank
    @Schema(description = "제목", example = "제목")
    private final String title;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private final String body;

    @Schema(description = "이미지 파일 목록")
    private final List<MultipartFile> images;

    public RequestCreateGenericPostDto(String title, String body, List<MultipartFile> images) {
        this.title = title;
        this.body = body;
        this.images = Objects.requireNonNullElseGet(images, ArrayList::new);
    }

    public abstract T toEntity(User user);
}
