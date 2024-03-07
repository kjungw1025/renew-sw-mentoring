package com.renew.sw.mentoring.domain.post.repository;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenericPostRepositoryTest {

    @Autowired
    private GenericPostRepository<Notice> noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {
        noticeRepository.deleteAll();

        Team team = Team.builder()
                .teamName("테스트팀")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("테스트유저")
                .nickname("테스트")
                .password("1234")
                .userRole(UserRole.USER)
                .studentId("201511111")
                .team(team)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .title("공지" + i)
                    .body("공지 내용" + i)
                    .user(user)
                    .build();
            noticeRepository.save(notice);
        }
    }

    @Test
    @DisplayName("게시글 전체 조회가 잘 되는지?")
    void findAll() {
        Page<Notice> result = noticeRepository.findAll((root, query, builder) -> null, Pageable.unpaged());

        assertThat(result.getTotalElements()).isEqualTo(3);
    }
}
