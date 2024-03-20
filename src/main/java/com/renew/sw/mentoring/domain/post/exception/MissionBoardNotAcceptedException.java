package com.renew.sw.mentoring.domain.post.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class MissionBoardNotAcceptedException extends LocalizedMessageException {
    public MissionBoardNotAcceptedException() { super(HttpStatus.BAD_REQUEST, "faild.missionBoard"); }
}
