package com.renew.sw.mentoring.domain.post.model.entity.dto.response;

import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseMissionBoardDto extends ResponseSingleGenericPostDto {

    @Schema(description = "댓글 개수", example = "4")
    private final int commentCount;

    @Schema(description = "게시글 상태", example = "IN_PROGRESS")
    private final RegisterStatus registerStatus;

    @Schema(description = "미션 아이디", example = "1")
    private final Long missionId;

    @Schema(description = "보너스 미션 성공 여부", example = "false")
    private final boolean bonusMissionSuccessful;

    public ResponseMissionBoardDto(ResponseSingleGenericPostDto dto, MissionBoard post) {
        super(dto);
        this.commentCount = post.getComments().size();
        this.registerStatus = post.getRegisterStatus();
        this.missionId = post.getMissionId();
        this.bonusMissionSuccessful = post.isBonusMissionSuccessful();
    }
}
