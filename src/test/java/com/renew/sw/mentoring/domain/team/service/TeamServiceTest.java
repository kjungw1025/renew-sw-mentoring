package com.renew.sw.mentoring.domain.team.service;

import com.renew.sw.mentoring.domain.team.exception.AlreadyTeamException;
import com.renew.sw.mentoring.domain.team.model.dto.list.SummarizedTeamDto;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.mock.UserMock;
import com.renew.sw.mentoring.util.DummyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private final List<Team> teamList = new ArrayList<>();

    @Test
    @DisplayName("이미 존재하는 팀이면 예외를 반환한다.")
    void checkAlreadyTeam() {
        // given
        Team team = Team.builder()
                .teamName("테스트팀")
                .build();
        when(teamRepository.findByTeamName(any())).thenReturn(Optional.of(team));

        // when & then
        AlreadyTeamException exception = assertThrows(AlreadyTeamException.class, () -> teamService.checkAlreadyTeam("테스트팀"));
        assertEquals(exception.getMessageId(),"already.team");
    }

    @Test
    @DisplayName("전체 팀 조회")
    void getAllTeams() {
        // given
        for (int i = 0; i < 3; i++) {
            Team team = Team.builder()
                    .teamName("팀" + i)
                    .build();
            teamList.add(team);
        }
        List<User> userList = UserMock.createList(teamList.get(0), 3);

        int TOTAL_ELEMENTS = 10;
        Page<Team> teamPage = new DummyPage<>(teamList, TOTAL_ELEMENTS);
        when(teamRepository.findAllDesc(any(Pageable.class))).thenReturn(teamPage);
        when(userRepository.findMentorByTeamId(any())).thenReturn(Optional.of(userList.get(0)));
        when(userRepository.findTeamMenteeByTeamId(any())).thenReturn(userList.subList(0, userList.size() - 1));

        // when
        Page<SummarizedTeamDto> list = teamService.getAllTeams(Pageable.unpaged());

        // then
        assertThat(list.getTotalElements()).isEqualTo(3);
        for (int i = 0; i < list.getTotalElements(); i++) {
            SummarizedTeamDto dto = list.getContent().get(i);
            assertThat(dto.getTeamName()).isEqualTo(teamList.get(i).getTeamName());
            assertThat(dto.getMembers()).isEqualTo(userList.stream().map(User::getName).toList());
        }
    }
}