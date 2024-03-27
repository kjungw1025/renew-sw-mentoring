package com.renew.sw.mentoring.domain.post.model.entity.dto.response;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseSingleNoticeDto extends ResponseSingleGenericPostDto {

    @Schema(description = "댓글 개수", example = "4")
    private final int commentCount;

    public ResponseSingleNoticeDto(ResponseSingleGenericPostDto dto, Notice notice) {
        super(dto);
        this.commentCount = notice.getComments().size();
    }
}
