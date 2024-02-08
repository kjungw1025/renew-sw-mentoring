package com.renew.sw.mentoring.global.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtAuthenticationToken implements AuthenticationToken {

    private String accessToken;
    private String refreshToken;
}
