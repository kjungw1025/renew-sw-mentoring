package com.renew.sw.mentoring.domain.team.model.dto.list;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class SummarizedTeamDto {

    @Schema(description = "팀 id", example = "1")
    private final Long id;

    @Schema(description = "팀명", example = "소웨1조")
    private final String teamName;

    @Schema(description = "점수", example = "500")
    private final int score;

    @Schema(description = "팀원", example = "[김멘토, 김멘티, 김멘티2]")
    private final List<String> members;

    public SummarizedTeamDto(Team team,
                             @NotNull List<String> members) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.score = team.getScore();
        this.members = members;
    }
}
