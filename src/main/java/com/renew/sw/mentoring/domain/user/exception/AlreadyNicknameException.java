package com.renew.sw.mentoring.domain.user.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyNicknameException extends LocalizedMessageException {
    public AlreadyNicknameException() {
        super(HttpStatus.BAD_REQUEST, "already.nickname");
    }
}
