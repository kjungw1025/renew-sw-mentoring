package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;

public class MissionMock {

    public static Mission create() {
        return Mission.builder()
                .name("mission")
                .description("description")
                .point(30)
                .difficulty(Difficulty.EASY)
                .missionStatus(MissionStatus.MAIN)
                .build();
    }
}
