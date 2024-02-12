package com.renew.sw.mentoring.domain.user.model;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserInfo {
    private final String name;
    private final String nickname;
    private final String studentId;
    private final String teamName;

    public UserInfo(User user) {
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.studentId = user.getStudentId();
        this.teamName = user.getTeam().getTeamName();
    }
}