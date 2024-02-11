package com.renew.sw.mentoring.domain.mission.model.entity;

import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class BonusMission extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bonus_mission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @NotBlank
    private String name;

    @Lob
    private String description;

    @NotNull
    private int point;

    @Builder
    private BonusMission(@NotNull Mission mission,
                        @NotNull String name,
                        String description,
                        @NotNull int point) {
        this.mission = mission;
        this.name = name;
        this.description = description;
        this.point = point;
    }
}
