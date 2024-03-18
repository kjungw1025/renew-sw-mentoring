package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RequestCreateNoticeDto extends RequestCreateGenericPostDto<Notice>{

    @Schema(description = "첨부파일 목록")
    private final List<MultipartFile> files;

    public RequestCreateNoticeDto(@NotBlank String title, @NotBlank String body, List<MultipartFile> images, List<MultipartFile> files) {
        super(title, body, images);
        this.files = files;
    }

    public Notice toEntity(User user) {
        return Notice.builder()
                .body(getBody())
                .title(getTitle())
                .user(user)
                .build();
    }
}
