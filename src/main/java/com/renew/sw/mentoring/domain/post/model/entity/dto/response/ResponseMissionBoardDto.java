package com.renew.sw.mentoring.domain.post.model.entity.dto.response;

import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import lombok.Getter;

@Getter
public class ResponseMissionBoardDto extends ResponseSingleGenericPostDto {
    public ResponseMissionBoardDto(ResponseSingleGenericPostDto dto, MissionBoard post) {
        super(dto);
    }
}
