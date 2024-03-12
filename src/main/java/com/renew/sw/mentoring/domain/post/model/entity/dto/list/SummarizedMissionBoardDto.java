package com.renew.sw.mentoring.domain.post.model.entity.dto.list;

import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.infra.s3.service.AWSObjectStorageService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SummarizedMissionBoardDto extends SummarizedGenericPostDto {

    @Schema(description = "미션 아이디", example = "1")
    private final Long missionId;

    @Schema(description = "보너스 미션 성공 여부", example = "false")
    private final boolean isBonusMissionSuccessful;

    @Schema(description = "등록 상태", example = "IN_PROGRESS")
    private final RegisterStatus registerStatus;

    public SummarizedMissionBoardDto(AWSObjectStorageService s3service, int bodySize, MissionBoard missionBoard) {
        super(s3service, bodySize, missionBoard);
        this.missionId = missionBoard.getMissionId();
        this.isBonusMissionSuccessful = missionBoard.isBonusMissionSuccessful();
        this.registerStatus = missionBoard.getRegisterStatus();
    }
}
