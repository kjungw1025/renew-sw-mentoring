package com.renew.sw.mentoring.util;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "dev")
public @interface FullIntegrationTest {
}
