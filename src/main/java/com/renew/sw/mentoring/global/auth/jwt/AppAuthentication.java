package com.renew.sw.mentoring.global.auth.jwt;

import com.renew.sw.mentoring.domain.user.model.UserRole;
import org.springframework.security.core.Authentication;

public interface AppAuthentication extends Authentication {
    Long getUserId();
    UserRole getUserRole();
    boolean isAdmin();
}
