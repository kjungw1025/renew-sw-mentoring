package com.renew.sw.mentoring.domain.post.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyMissionBoardAcceptedException extends LocalizedMessageException {
    public AlreadyMissionBoardAcceptedException() { super(HttpStatus.BAD_REQUEST, "already.mission-board.accepted"); }
}
