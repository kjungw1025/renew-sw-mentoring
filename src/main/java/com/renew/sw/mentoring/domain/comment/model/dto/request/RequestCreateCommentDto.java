package com.renew.sw.mentoring.domain.comment.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCreateCommentDto {

    @NotBlank
    private String content;

    public RequestCreateCommentDto(String content) {
        this.content = content;
    }
}
