package com.renew.sw.mentoring.infra.s3.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidImageContentTypeException extends LocalizedMessageException {

    public InvalidImageContentTypeException(Throwable e) {
        super(e, HttpStatus.BAD_REQUEST, "invalid.image-content-type");
    }
}
