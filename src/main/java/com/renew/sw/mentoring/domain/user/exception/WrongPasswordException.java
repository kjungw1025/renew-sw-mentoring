package com.renew.sw.mentoring.domain.user.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends LocalizedMessageException {
    public WrongPasswordException() {
        super(HttpStatus.BAD_REQUEST, "invalid.password");
    }
}
