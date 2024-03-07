package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.global.auth.jwt.JwtAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuth {

    public static void withUser(Long userId) {
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthentication(userId, UserRole.USER));
    }

    public static void withMentor(Long userId) {
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthentication(userId, UserRole.MENTOR));
    }

    public static void withMentee(Long userId) {
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthentication(userId, UserRole.MENTEE));
    }

    public static void withAdmin(Long userId) {
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthentication(userId, UserRole.ADMIN));
    }
}
