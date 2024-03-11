package com.renew.sw.mentoring.domain.team.model.dto.response;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class ResponseTeamInfoDto {

    @Schema(description = "팀명", example = "소웨1조")
    private final String teamName;

    @Schema(description = "총 점수", example = "100")
    private final int score;

    @Schema(description = "멘토", example = "김멘토")
    private final String mentor;

    @Schema(description = "팀원", example = "[김멘토, 김멘티, 김멘티2]")
    private final List<String> members;

    public ResponseTeamInfoDto(Team team,
                               @NotNull String mentor,
                               @NotNull List<String> members) {
        this.teamName = team.getTeamName();
        this.score = team.getScore();
        this.mentor = mentor;
        this.members = members;
    }
}
