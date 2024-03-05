package com.renew.sw.mentoring.domain.team.model.entity;

import com.renew.sw.mentoring.domain.completedmission.model.entity.CompletedMission;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @NotBlank
    private String teamName;

    @NotNull
    private int score;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<CompletedMission> completedMissions = new ArrayList<>();

    private boolean isAdminTeam;

    @Builder
    private Team(@NotNull String teamName) {
        this.teamName = teamName;
        this.score = 0;
        this.isAdminTeam = false;
    }

    public void addScore(int point) {
        this.score += point;
    }

    public void setAdminTeam() {
        this.isAdminTeam = true;
    }
}
