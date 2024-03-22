package com.renew.sw.mentoring.domain.comment.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends LocalizedMessageException {

    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.comment");
    }
}
