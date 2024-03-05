package com.renew.sw.mentoring.domain.completedmission.model.entity;

import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.global.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class CompletedMission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "completed_mission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private boolean isBonusSuccess;

    @Builder
    private CompletedMission(Team team,
                             Mission mission,
                             boolean isBonusSuccess) {
        this.team = team;
        this.mission = mission;
        this.isBonusSuccess = isBonusSuccess;
    }
}
