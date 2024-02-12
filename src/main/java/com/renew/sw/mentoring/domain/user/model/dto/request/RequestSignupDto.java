package com.renew.sw.mentoring.domain.user.model.dto.request;

import lombok.Getter;

@Getter
public class RequestSignupDto {
    private final String studentId;
    private final String password;
    private final String name;
    private final String teamName;

    public RequestSignupDto(String studentId, String password, String name, String teamName) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.teamName = teamName;
    }
}
