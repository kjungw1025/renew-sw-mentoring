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

    @Schema(description = "총 점수", example = "50")
    private final int score;

    @Schema(description = "멘토", example = "제니")
    private final String mentor;

    @Schema(description = "팀 멤버", example = "제니, 지수, 로제, 리사")
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
