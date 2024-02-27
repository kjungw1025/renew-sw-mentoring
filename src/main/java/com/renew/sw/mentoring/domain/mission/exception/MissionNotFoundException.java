package com.renew.sw.mentoring.domain.mission.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class MissionNotFoundException extends LocalizedMessageException {

    public MissionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.mission");
    }
}
