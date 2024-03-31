package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.util.EntityUtil;
import com.renew.sw.mentoring.util.RandomGen;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserMock {
    public static final String STUDENT_ID = "12345678";
    public static final String PASSWORD = "abcdabab";

    public static final String NAME = "username";
    public static final String NICKNAME = "nickname";
    public static User create(Long userId, Team team, UserRole userRole, PasswordEncoder passwordEncoder) {
        String password = PASSWORD;

        if (passwordEncoder != null) {
            password = passwordEncoder.encode(password);
        }

        User user = User.builder()
                .team(team)
                .studentId(STUDENT_ID)
                .password(password)
                .name(NAME)
                .nickname(NICKNAME)
                .userRole(userRole)
                .build();
        EntityUtil.injectId(User.class, user, userId);
        return user;
    }

    public static User create(Team team, UserRole userRole, PasswordEncoder passwordEncoder) {
        return create(RandomGen.nextLong(), team, userRole, passwordEncoder);
    }

    public static User create(Team team, UserRole userRole) {
        return create(RandomGen.nextLong(), team, userRole, null);
    }

    public static User create(Long userId, Team team, UserRole userRole) {
        return create(userId, team, userRole, null);
    }

    public static List<User> createList(Team team, int size) {
        List<User> users = new ArrayList<>();
        users.add(create(team, UserRole.MENTOR));
        for (int i = 0; i < size - 1; i++) {
            users.add(create(team, UserRole.MENTEE));
        }
        return users;
    }
}
