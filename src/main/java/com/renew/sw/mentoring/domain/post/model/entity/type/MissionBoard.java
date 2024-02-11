package com.renew.sw.mentoring.domain.post.model.entity.type;

import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private RegisterStatus registerStatus;

    @Builder
    private MissionBoard(@NotNull User user,
                         @NotNull String title,
                         @NotNull String body,
                         @NotNull Mission mission,
                         @NotNull RegisterStatus registerStatus) {
        super(user, title, body);
        this.mission = mission;
        this.registerStatus = registerStatus;
    }
}
