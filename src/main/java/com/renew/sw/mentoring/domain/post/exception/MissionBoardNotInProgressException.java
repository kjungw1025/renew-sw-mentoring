package com.renew.sw.mentoring.domain.post.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class MissionBoardNotInProgressException extends LocalizedMessageException {
    public MissionBoardNotInProgressException() { super(HttpStatus.BAD_REQUEST, "failed.mission-board.in-progress"); }
}
