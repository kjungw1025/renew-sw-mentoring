package com.renew.sw.mentoring.domain.post.controller;

import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.GenericPostRepository;
import com.renew.sw.mentoring.domain.post.repository.NoticeRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest extends AbstractContainerRedisTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private final List<Notice> noticeList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        teamRepository.deleteAll();
        noticeRepository.deleteAll();
    }

    @Test
    @DisplayName("공지사항 전체 조회")
    void list() throws Exception {
        // given
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .team(team)
                .build();
        userRepository.save(user);


        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .title("title" + i)
                    .body("body" + i)
                    .user(user)
                    .build();
            noticeList.add(notice);
            noticeRepository.save(notice);
        }

        // when
        ResultActions result = mvc.perform(get("/notice"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(3)))
                .andExpect(jsonPath("content[0].title", is(noticeList.get(0).getTitle())))
                .andExpect(jsonPath("content[0].body", is(noticeList.get(0).getBody())))
                .andExpect(jsonPath("content[0].author", is(user.getNickname())));
    }

    @Test
    @DisplayName("공지사항 단일 조회")
    void findOne() throws Exception {
        // given
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .team(team)
                .build();
        userRepository.save(user);
        UserAuth.withUser(user.getId());


        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .title("title" + i)
                    .body("body" + i)
                    .user(user)
                    .build();
            noticeList.add(notice);
        }
        noticeRepository.saveAll(noticeList);

        // when
        ResultActions result = mvc.perform(get("/notice/" + noticeList.get(0).getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(noticeList.get(0).getId().intValue())))
                .andExpect(jsonPath("title", is(noticeList.get(0).getTitle())))
                .andExpect(jsonPath("body", is(noticeList.get(0).getBody())))
                .andExpect(jsonPath("author", is(user.getNickname())))
                .andExpect(jsonPath("mine", is(true)));
    }

    @Test
    @DisplayName("공지사항 단일 조회 실패")
    void findOneFail() throws Exception {
        // given
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .team(team)
                .build();
        userRepository.save(user);
        UserAuth.withUser(user.getId());

        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .user(user)
                .build();
        noticeRepository.save(notice);

        // when
        ResultActions result = mvc.perform(get("/notice/" + 0L));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공지사항 글 작성")
    void create() throws Exception {
        // given
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.ADMIN)
                .team(team)
                .build();
        userRepository.save(user);
        UserAuth.withAdmin(user.getId());

        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .user(user)
                .build();
        noticeRepository.save(notice);

        // when
        ResultActions result = mvc.perform(multipart("/notice")
                .param("title", "title")
                .param("body", "body"));

        // then
        MvcResult response = result.andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andReturn();
    }

    @Test
    @DisplayName("공지사항 글 삭제 성공")
    void delete() throws Exception{
        // given
        Team team = Team.builder()
                .teamName("teamName")
                .build();
        teamRepository.save(team);

        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.ADMIN)
                .team(team)
                .build();
        userRepository.save(user);
        UserAuth.withAdmin(user.getId());

        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .user(user)
                .build();
        noticeRepository.save(notice);

        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/notice/" + notice.getId()));

        // then
        result.andExpect(status().isOk());
        assertThat(noticeRepository.findById(notice.getId())).isEmpty();
    }
}