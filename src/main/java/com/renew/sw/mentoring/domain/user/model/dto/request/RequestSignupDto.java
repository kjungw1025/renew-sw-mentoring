package com.renew.sw.mentoring.domain.user.model.dto.request;

import lombok.Getter;

@Getter
public class RequestSignupDto {
    private final String studentId;
    private final String password;
    private final String name;
    private final String nickname;

    public RequestSignupDto(String studentId, String password, String name, String nickname) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }
}
