package com.renew.sw.mentoring.domain.post.exception;

import com.renew.sw.mentoring.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends LocalizedMessageException {

    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.post");
    }
}
