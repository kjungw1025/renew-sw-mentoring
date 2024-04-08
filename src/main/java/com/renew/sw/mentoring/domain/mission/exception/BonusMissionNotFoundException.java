package com.renew.sw.mentoring.domain.mission.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class BonusMissionNotFoundException extends LocalizedMessageException {

    public BonusMissionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.bonusMission");
    }
}
