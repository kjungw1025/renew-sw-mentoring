package com.renew.sw.mentoring.domain.user.model.dto.response;

import com.renew.sw.mentoring.global.auth.jwt.AuthenticationToken;
import lombok.Getter;

@Getter
public class ResponseLoginDto {

    private final String accessToken;
    private final String refreshToken;

    public ResponseLoginDto(AuthenticationToken token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
