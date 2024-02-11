package com.renew.sw.mentoring.domain.post.model.entity.type;

import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * 공지 게시글
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Post {
    @Builder
    private Notice(@NotNull User user,
                   @NotNull String title,
                   @NotNull String body) {
        super(user, title, body);
    }
}
