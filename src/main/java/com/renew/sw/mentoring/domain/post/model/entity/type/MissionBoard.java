package com.renew.sw.mentoring.domain.post.model.entity.type;

import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionBoard extends Post {

    private Long missionId;

    private boolean isBonusMissionSuccessful;

    @Enumerated(EnumType.STRING)
    private RegisterStatus registerStatus;

    @Builder
    private MissionBoard(@NotNull User user,
                         @NotNull String title,
                         @NotNull String body,
                         @NotNull Long missionId,
                         boolean isBonusMissionSuccessful,
                         @NotNull RegisterStatus registerStatus) {
        super(user, title, body);
        this.missionId = missionId;
        this.isBonusMissionSuccessful = isBonusMissionSuccessful;
        this.registerStatus = registerStatus;
    }

    public void changeRegisterStatus(RegisterStatus registerStatus) {
        this.registerStatus = registerStatus;
    }
}
