package com.renew.sw.mentoring.domain.admin.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCreateAdminDto {

    private String studentId;
    private String password;
    private String name;
    private String nickname;

    public RequestCreateAdminDto(String studentId, String password, String name, String nickname) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }
}
