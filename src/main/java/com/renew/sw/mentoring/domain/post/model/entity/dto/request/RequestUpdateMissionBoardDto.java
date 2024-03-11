package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RequestUpdateMissionBoardDto extends RequestUpdateGenericPostDto<MissionBoard> {

    @Schema(description = "미션 id", example = "1")
    private final Long missionId;

    @Schema(description = "보너스 문제 성공 여부", example = "true")
    private final Boolean isBonusMissionSuccessful;
    public RequestUpdateMissionBoardDto(String title,
                                        String body,
                                        @NotBlank Long missionId,
                                        Boolean isBonusMissionSuccessful) {
        super(title, body);
        this.missionId = missionId;
        this.isBonusMissionSuccessful = isBonusMissionSuccessful;
    }

    @Override
    public MissionBoard toEntity(User user) {
        return MissionBoard.builder()
                .user(user)
                .title(getTitle())
                .body(getBody())
                .missionId(missionId)
                .isBonusMissionSuccessful(isBonusMissionSuccessful != null && isBonusMissionSuccessful)
                .registerStatus(RegisterStatus.IN_PROGRESS)
                .build();
    }
}
