package com.renew.sw.mentoring.global.auth.role;

import com.renew.sw.mentoring.global.auth.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecurityRequirement(name = JwtTokenProvider.AUTHORIZATION)
@Secured(UserAuthNames.ROLE_MENTOR)
public @interface MentorAuth {
}
