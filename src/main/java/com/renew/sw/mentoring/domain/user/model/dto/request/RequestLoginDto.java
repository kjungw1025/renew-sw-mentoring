package com.renew.sw.mentoring.domain.user.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestLoginDto {
    private String studentId;
    private String password;

    public RequestLoginDto(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }
}
