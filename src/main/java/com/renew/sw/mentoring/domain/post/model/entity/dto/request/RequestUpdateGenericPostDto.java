package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public abstract class RequestUpdateGenericPostDto<T> {

    @Schema(description = "제목", example = "제목")
    private final String title;

    @Schema(description = "본문", example = "내용")
    private final String body;

    public RequestUpdateGenericPostDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public abstract T toEntity(User user);
}
