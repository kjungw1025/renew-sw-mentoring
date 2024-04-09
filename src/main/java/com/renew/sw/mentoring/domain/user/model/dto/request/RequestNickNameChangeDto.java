package com.renew.sw.mentoring.domain.user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class RequestNickNameChangeDto {
    @NotBlank
    @Size(min = 3, max = 16)
    @Schema(description = "새로운 닉네임", example = "단국대 빌게이츠")
    private final String nickname;

    @JsonCreator
    public RequestNickNameChangeDto(String nickname) {
        this.nickname = nickname;
    }
}
