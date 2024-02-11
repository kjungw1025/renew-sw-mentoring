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
 * 멘토와 멘티 둘 다 사용할 수 있는 권한
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
@SecurityRequirement(name = JwtTokenProvider.AUTHORIZATION)
@Secured(UserAuthNames.ROLE_USER)
public @interface UserAuth {
}
