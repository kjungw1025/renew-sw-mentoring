package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import lombok.Getter;

@Getter
public class RequestUpdateNoticeDto extends RequestUpdateGenericPostDto<Notice> {
    public RequestUpdateNoticeDto(String title, String body) {
        super(title, body);
    }

    @Override
    public Notice toEntity(User user) {
        return Notice.builder()
                .title(getTitle())
                .body(getBody())
                .user(user)
                .build();
    }
}
