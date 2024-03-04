package com.renew.sw.mentoring.domain.team.model.dto.list;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SummarizedTeamDto {

    @Schema(description = "팀 id", example = "1")
    private final Long id;

    @Schema(description = "팀명", example = "소웨1조")
    private final String teamName;

    @Schema(description = "점수", example = "500")
    private final int score;

    public SummarizedTeamDto(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.score = team.getScore();
    }
}
