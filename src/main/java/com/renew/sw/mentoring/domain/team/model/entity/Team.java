package com.renew.sw.mentoring.domain.team.model.entity;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.global.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
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

    @Builder
    private Team(@NotNull String teamName,
                @NotNull int score) {
        this.teamName = teamName;
        this.score = score;
    }
}
