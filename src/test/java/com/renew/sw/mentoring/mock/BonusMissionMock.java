package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;

public class BonusMissionMock {

    public static BonusMission create(Mission mission) {
        return BonusMission.builder()
                .mission(mission)
                .name("bonusMission")
                .description("description")
                .point(20)
                .build();
    }
}
