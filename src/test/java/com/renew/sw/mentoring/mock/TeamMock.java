package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.team.model.entity.Team;

public class TeamMock {
    public static Team create(String teamName) {
        return Team.builder()
                .teamName(teamName)
                .build();
    }
}
