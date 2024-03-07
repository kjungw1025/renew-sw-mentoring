package com.renew.sw.mentoring.domain.post.model.entity.dto.response;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import lombok.Getter;

@Getter
public class ResponseSingleNoticeDto extends ResponseSingleGenericPostDto {

    public ResponseSingleNoticeDto(ResponseSingleGenericPostDto dto, Notice notice) {
        super(dto);
    }
}
