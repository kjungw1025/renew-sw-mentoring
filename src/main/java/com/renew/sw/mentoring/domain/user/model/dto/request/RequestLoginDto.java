package com.renew.sw.mentoring.domain.user.model.dto.request;

import lombok.Getter;

@Getter
public class RequestLoginDto {
    private final String studentId;
    private final String password;

    public RequestLoginDto(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }
}
