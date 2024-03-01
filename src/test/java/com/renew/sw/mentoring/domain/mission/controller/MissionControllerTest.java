package com.renew.sw.mentoring.domain.mission.controller;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MissionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private BonusMissionRepository bonusMissionRepository;

    @Autowired
    private MessageSource messageSource;

    private final List<Mission> missionList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        bonusMissionRepository.deleteAll();
        missionRepository.deleteAll();
    }

    @Test
    @DisplayName("미션 전체 조회")
    void list() throws Exception {

        // given
        for (int i = 0; i < 3; i++) {
            Mission mission = Mission.builder()
                    .name("미션" + i)
                    .description("미션" + i + "설명")
                    .point(10)
                    .difficulty(Difficulty.EASY)
                    .missionStatus(MissionStatus.MAIN)
                    .build();
            missionList.add(mission);
            missionRepository.save(mission);
        }

        // when
        ResultActions result = mvc.perform(get("/mission/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(3)))
                .andExpect(jsonPath("content[0].id", is(missionList.get(0).getId().intValue())))
                .andExpect(jsonPath("content[0].name", is("미션0")))
                .andExpect(jsonPath("content[0].description", is("미션0설명")))
                .andExpect(jsonPath("content[0].point", is(10)))
                .andExpect(jsonPath("content[0].difficulty", is(
                        messageSource.getMessage("mission.difficulty." + missionList.get(0).getDifficulty().getName().toLowerCase(),
                                null, LocaleContextHolder.getLocale())
                )))
                .andExpect(jsonPath("content[0].missionStatus", is("MAIN")));
    }

    @Test
    @DisplayName("미션 단일 조회")
    void findOne() throws Exception{

        // given
        Mission mission = Mission.builder()
                .name("미션")
                .description("미션설명")
                .point(30)
                .difficulty(Difficulty.NORMAL)
                .missionStatus(MissionStatus.MAIN)
                .build();
        missionRepository.save(mission);

        // when
        ResultActions result = mvc.perform(get("/mission/" + mission.getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(mission.getId().intValue())))
                .andExpect(jsonPath("name", is("미션")))
                .andExpect(jsonPath("description", is("미션설명")))
                .andExpect(jsonPath("point", is(30)))
                .andExpect(jsonPath("difficulty", is(
                        messageSource.getMessage("mission.difficulty." + mission.getDifficulty().getName().toLowerCase(),
                                null, LocaleContextHolder.getLocale())
                )))
                .andExpect(jsonPath("missionStatus", is("MAIN")));
    }
}