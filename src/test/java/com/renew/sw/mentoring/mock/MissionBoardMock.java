package com.renew.sw.mentoring.mock;

import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.user.model.entity.User;

public class MissionBoardMock {
    public static MissionBoard create(User user,
                                      Long missionId,
                                      boolean isBonusMissionSuccessful,
                                      RegisterStatus registerStatus) {
        return MissionBoard.builder()
                .user(user)
                .title("title")
                .body("body")
                .missionId(missionId)
                .isBonusMissionSuccessful(isBonusMissionSuccessful)
                .registerStatus(registerStatus)
                .build();
    }
}
