package com.renew.sw.mentoring.domain.mission.model.dto.response;

import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.dto.list.ResponseBonusMissionDto;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

@Getter
public class ResponseSingleMissionDto {

    @Schema(description = "미션 id", example = "1")
    private final Long id;

    @Schema(description = "미션 이름", example = "미션")
    private final String name;

    @Schema(description = "미션 설명", example = "미션 설명")
    private final String description;

    @Schema(description = "미션 포인트", example = "100")
    private final int point;

    @Schema(description = "미션 난이도", example = "하")
    private final String difficulty;

    @Schema(description = "미션 상태", example = "미션 상태")
    private final MissionStatus missionStatus;

    @Schema(description = "보너스 미션 목록")
    private final List<ResponseBonusMissionDto> bonusMissionList;

    public ResponseSingleMissionDto(Mission mission, List<BonusMission> bonusMissions, MessageSource messageSource) {
        this.id = mission.getId();
        this.name = mission.getName();
        this.description = mission.getDescription();
        this.point = mission.getPoint();
        this.difficulty = messageSource.getMessage("mission.difficulty." + mission.getDifficulty().getName().toLowerCase(),
                null, LocaleContextHolder.getLocale());
        this.missionStatus = mission.getMissionStatus();
        this.bonusMissionList = ResponseBonusMissionDto.listOf(bonusMissions);
    }
}
