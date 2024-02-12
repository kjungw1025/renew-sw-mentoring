package com.renew.sw.mentoring.domain.team.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends LocalizedMessageException {
    public TeamNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.team"); }
}
