package com.renew.sw.mentoring.infra.s3.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidAccessObjectStorageException extends LocalizedMessageException {
    public InvalidAccessObjectStorageException(Throwable e) {
        super(e, HttpStatus.BAD_REQUEST, "invalid.access-s3");
    }
}
