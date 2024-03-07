package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.NoticeRepository;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.mock.UserAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 게시글이 잘 삭제되는가?")
    void delete() {
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.ADMIN)
                .team(team)
                .build();
        UserAuth.withAdmin(user.getId());
        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .user(user)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(notice, "user", user);

        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));

        // when
        noticeService.delete(1L, notice.getId(), notice.getUser().getUserRole());

        // then
        verify(noticeRepository).delete(argThat(notice::equals));
    }

    @Test
    @DisplayName("없는 게시글 삭제 시 오류")
    void deleteWithNotFountPost() {
        // given
        when(noticeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () ->
                noticeService.delete(1L, 1L, UserRole.ADMIN));
    }
}