package com.renew.sw.mentoring.domain.team.repository;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.GenericPostRepository;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.util.FullIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@FullIntegrationTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenericPostRepository<Notice> noticeRepository;

    private final List<String> teamList = List.of("팀1", "팀2", "팀3", "팀4", "관리자팀");

    @BeforeEach
    void setUp() {
        noticeRepository.deleteAll();
        userRepository.deleteAll();
        teamRepository.deleteAll();

        for (int i = 0; i < 4; i++) {
            Team team = Team.builder()
                    .teamName(teamList.get(i))
                    .build();
            teamRepository.save(team);
        }

        Team team = Team.builder()
                .teamName(teamList.get(4))
                .build();
        team.setAdminTeam();
        teamRepository.save(team);
    }

    @Test
    @DisplayName("관리자팀을 제외한 전체 팀 조회가 잘 되는지?")
    void findAllTeam() {
        // given & when
        Page<Team> teams = teamRepository.findAllDesc(Pageable.unpaged());

        // then
        assertThat(teams.getTotalElements()).isEqualTo(4);
    }
}