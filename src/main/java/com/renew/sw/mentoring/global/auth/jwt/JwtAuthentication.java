package com.renew.sw.mentoring.global.auth.jwt;

import com.renew.sw.mentoring.domain.user.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class JwtAuthentication implements AppAuthentication {

    private final Long userId;
    private final UserRole userRole;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public UserRole getUserRole() {
        return userRole;
    }

    @Override
    public boolean isAdmin() {
        return userRole.isAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : userRole.getName().split(",")) {
            authorities.add(() -> authority);
        }
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return userId;
    }

    @Override
    public Object getDetails() {
        return userId;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
