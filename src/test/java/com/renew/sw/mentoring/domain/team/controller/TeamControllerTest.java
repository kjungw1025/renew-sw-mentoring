package com.renew.sw.mentoring.domain.team.controller;

import com.renew.sw.mentoring.domain.completedmission.repository.CompletedMissionRepository;
import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.GenericPostRepository;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.mock.UserAuth;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
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

    @Autowired
    private GenericPostRepository<Notice> noticeRepository;

    @Autowired
    private CompletedMissionRepository completedMissionRepository;

    private final List<String> teamNameList = List.of("팀1", "팀2", "팀3", "팀4", "관리자팀");
    private final List<Team> teamList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        completedMissionRepository.deleteAll();
        noticeRepository.deleteAll();
        userRepository.deleteAll();
        teamRepository.deleteAll();

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
        ResultActions result = mvc.perform(get("/team"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(4)))
                .andExpect(jsonPath("content[0].id", is(teamList.get(0).getId().intValue())))
                .andExpect(jsonPath("content[0].teamName", is("팀1")))
                .andExpect(jsonPath("content[0].score", is(0)));
    }

    @Test
    @DisplayName("우리 팀 상세 조회 (팀원, 멘토, 총 점수)")
    void checkMyTeam() throws Exception {
        // given
        User mentorUser = User.builder()
                .name("mentorUser")
                .nickname("mentor")
                .password("password")
                .studentId("1234")
                .userRole(UserRole.MENTOR)
                .team(teamList.get(1))
                .build();
        userRepository.save(mentorUser);
        UserAuth.withMentor(mentorUser.getId());

        User menteeUser = User.builder()
                .name("menteeUser")
                .nickname("mentee")
                .password("password")
                .studentId("5678")
                .userRole(UserRole.MENTEE)
                .team(teamList.get(1))
                .build();
        userRepository.save(menteeUser);
        UserAuth.withMentee(menteeUser.getId());

        // when
        ResultActions result = mvc.perform(get("/team/my"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("teamName", is(teamList.get(1).getTeamName())))
                .andExpect(jsonPath("score", is(teamList.get(1).getScore())))
                .andExpect(jsonPath("mentor", is(mentorUser.getName())))
                .andExpect(jsonPath("members", containsInAnyOrder(mentorUser.getName(), menteeUser.getName())));
    }
}