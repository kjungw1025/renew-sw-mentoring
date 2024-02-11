package com.renew.sw.mentoring.global.auth.role;

import com.renew.sw.mentoring.global.auth.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * Admin 권한
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
@SecurityRequirement(name = JwtTokenProvider.AUTHORIZATION)
@Secured(UserAuthNames.ROLE_ADMIN)
public @interface AdminAuth {
}
