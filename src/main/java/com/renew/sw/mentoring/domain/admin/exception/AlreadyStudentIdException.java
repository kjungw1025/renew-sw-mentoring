package com.renew.sw.mentoring.domain.admin.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyStudentIdException extends LocalizedMessageException {

    public AlreadyStudentIdException() {
        super(HttpStatus.BAD_REQUEST, "already.student.id");
    }
}
