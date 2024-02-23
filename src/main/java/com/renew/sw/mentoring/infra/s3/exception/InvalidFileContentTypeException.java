package com.renew.sw.mentoring.infra.s3.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidFileContentTypeException extends LocalizedMessageException {

    public InvalidFileContentTypeException(Throwable e) {
        super(e, HttpStatus.BAD_REQUEST, "invalid.file-content-type");
    }
}
