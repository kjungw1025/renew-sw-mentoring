package com.renew.sw.mentoring.global.auth.role;

public class UserAuthNames {
    public static final String ROLE_MENTEE = "ROLE_MENTEE";
    public static final String ROLE_MENTOR = "ROLE_MENTOR";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static String combine(String... names) {
        return String.join(",", names);
    }
}
