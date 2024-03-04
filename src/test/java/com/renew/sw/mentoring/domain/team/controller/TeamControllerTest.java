package com.renew.sw.mentoring.domain.team.controller;

import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.util.AbstractContainerRedisTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class TeamControllerTest extends AbstractContainerRedisTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private final List<String> teamNameList = List.of("팀1", "팀2", "팀3", "팀4", "관리자팀");
    private final List<Team> teamList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        userRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();

        for (int i = 0; i < 4; i++) {
            Team team = Team.builder()
                    .teamName(teamNameList.get(i))
                    .build();
            teamList.add(team);
            teamRepository.save(team);
        }

        Team team = Team.builder()
                .teamName(teamNameList.get(4))
                .build();
        team.setAdminTeam();
        teamList.add(team);
        teamRepository.save(team);
    }

    @Test
    @DisplayName("전체 팀 조회 - 관리자 팀은 조회되지 않아야 한다.")
    void list() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/team/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(4)))
                .andExpect(jsonPath("content[0].id", is(teamList.get(0).getId().intValue())))
                .andExpect(jsonPath("content[0].teamName", is("팀1")))
                .andExpect(jsonPath("content[0].score", is(0)));
    }
}