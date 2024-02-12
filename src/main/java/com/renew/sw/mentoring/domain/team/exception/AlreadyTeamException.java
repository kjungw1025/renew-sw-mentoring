package com.renew.sw.mentoring.domain.team.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyTeamException extends LocalizedMessageException {
    public AlreadyTeamException() { super(HttpStatus.BAD_REQUEST, "already.team"); }
}
