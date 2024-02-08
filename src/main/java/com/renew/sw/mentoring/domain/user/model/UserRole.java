package com.renew.sw.mentoring.domain.user.model;

import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.renew.sw.mentoring.global.auth.role.UserAuthNames.*;

@Getter
public enum UserRole {
    MENTEE(ROLE_MENTEE),
    MENTOR(ROLE_MENTOR),
    USER(combine(ROLE_MENTOR, ROLE_MENTEE)),
    ADMIN(combine(ROLE_MENTOR, ROLE_MENTEE, ROLE_ADMIN));

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    private static final Map<String, UserRole> BY_LABEL =
            Stream.of(values()).collect(Collectors.toMap(UserRole::getName, e -> e));

    public static UserRole of(String name) {
        return BY_LABEL.get(name);
    }

    public static UserRole from(AppAuthentication auth) {
        return auth.getUserRole();
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
