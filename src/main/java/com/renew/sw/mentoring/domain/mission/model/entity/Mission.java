package com.renew.sw.mentoring.domain.mission.model.entity;

import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Mission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    @NotBlank
    private String name;

    @Lob
    private String description;

    @NotNull
    private int point;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @OneToMany(mappedBy = "mission")
    private List<BonusMission> bonusMissions = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<MissionBoard> missionBoards = new ArrayList<>();

    @Builder
    private Mission(@NotNull String name,
                    String description,
                    @NotNull int point,
                    MissionStatus missionStatus) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.missionStatus = missionStatus;
    }
}
