package com.renew.sw.mentoring.domain.user.model.entity;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotBlank
    @Column
    private String studentId;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @NotBlank
    @Column(length = 20)
    private String name;

    @NotBlank
    @Column(length = 20)
    private String nickname;

    @Builder
    private User(@NotNull Team team,
                @NotNull String studentId,
                @NotNull String password,
                @NotNull String name,
                @NotNull String nickname,
                UserRole userRole) {
        this.team = team;
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    /**
     * 닉네임을 변경합니다.
     *
     * @param nickname 닉네임
     */
    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }
}
