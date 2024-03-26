package com.renew.sw.mentoring.domain.user.model.dto.response;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
@RequiredArgsConstructor
public class ResponseUserInfoDto {
    private final String studentId;
    private final String username;
    private final String nickname;
    private final String teamName;
    private final String role;
    private final boolean isAdmin;

    public ResponseUserInfoDto(User user, MessageSource messageSource) {
        this.studentId = user.getStudentId();
        this.username = user.getName();
        this.nickname = user.getNickname();
        this.teamName = user.getTeam().getTeamName();
        this.role = messageSource.getMessage("role." + user.getUserRole().name().toLowerCase(), null, LocaleContextHolder.getLocale());
        this.isAdmin = user.getUserRole().isAdmin();
    }
}
