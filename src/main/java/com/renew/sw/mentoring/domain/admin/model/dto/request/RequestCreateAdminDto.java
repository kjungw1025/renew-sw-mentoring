package com.renew.sw.mentoring.domain.admin.model.dto.request;

import lombok.Getter;

@Getter
public class RequestCreateAdminDto {

    private final String studentId;
    private final String password;
    private final String name;
    private final String nickname;

    public RequestCreateAdminDto(String studentId, String password, String name, String nickname) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }
}
