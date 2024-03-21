package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMock {
    public static final String STUDENT_ID = "12345678";
    public static final String PASSWORD = "abcdabab";

    public static final String NAME = "username";
    public static final String NICKNAME = "nickname";
    public static User create(Team team, UserRole userRole, PasswordEncoder passwordEncoder) {
        String password = PASSWORD;

        if (passwordEncoder != null) {
            password = passwordEncoder.encode(password);
        }

        return User.builder()
                .team(team)
                .studentId(STUDENT_ID)
                .password(password)
                .name(NAME)
                .nickname(NICKNAME)
                .userRole(userRole)
                .build();
    }
}
