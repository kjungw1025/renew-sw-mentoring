package com.renew.sw.mentoring.domain.mission.service;

import com.renew.sw.mentoring.domain.mission.exception.MissionNotFoundException;
import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.dto.list.SummarizedMissionDto;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import com.renew.sw.mentoring.util.DummyPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private BonusMissionRepository bonusMissionRepository;

    @Mock
    private MessageSource messageSource;

    private final List<Mission> missionList = new ArrayList<>();

    private final List<BonusMission> bonusMissionList = new ArrayList<>();

    @InjectMocks
    private MissionService missionService;

    @BeforeEach
    public void setup() {
        bonusMissionRepository.deleteAll();
        missionRepository.deleteAll();
    }

    @Test
    @DisplayName("미션 전체 조회")
    void list() {
        // given
        for (int i = 0; i < 3; i++) {
            Mission mission = Mission.builder()
                    .name("미션" + i)
                    .description("미션 설명" + i)
                    .missionStatus(MissionStatus.MAIN)
                    .difficulty(Difficulty.HARD)
                    .point(70)
                    .build();
            missionList.add(mission);
        }

        Page<Mission> missionPage = new DummyPage<>(missionList, 20);
        when(missionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(missionPage);

        // when
        Page<SummarizedMissionDto> list = missionService.list(null, Pageable.unpaged(), null, false);

        // then
        assertThat(list.getTotalElements()).isEqualTo(3);
        for (int i = 0; i < list.getTotalElements(); i++) {
            SummarizedMissionDto dto = list.getContent().get(i);
            assertThat(dto.getId()).isEqualTo(missionList.get(i).getId());
            assertThat(dto.getName()).isEqualTo(missionList.get(i).getName());
            assertThat(dto.getDescription()).isEqualTo(missionList.get(i).getDescription());
            assertThat(dto.getPoint()).isEqualTo(missionList.get(i).getPoint());
            assertThat(dto.getDifficulty()).isEqualTo(messageSource.getMessage("mission.difficulty." + missionList.get(i).getDifficulty().getName().toLowerCase(),
                    null, LocaleContextHolder.getLocale()));
            assertThat(dto.getMissionStatus()).isEqualTo(missionList.get(i).getMissionStatus());
        }
    }

    @Test
    @DisplayName("미션 전체 조회 - 보너스 미션이 있는 경우")
    void listWithHavingBonusMission() {
        // given
        for (int i = 0; i < 3; i++) {
            Mission mission = Mission.builder()
                    .name("미션" + i)
                    .description("미션 설명" + i)
                    .missionStatus(MissionStatus.MAIN)
                    .difficulty(Difficulty.HARD)
                    .point(70)
                    .build();
            missionList.add(mission);
            BonusMission bm = BonusMission.builder()
                    .name("보너스 미션" + i)
                    .description("보너스 미션 설명" + i)
                    .point(30)
                    .mission(mission)
                    .build();
            bonusMissionList.add(bm);
        }

        Page<Mission> missionPage = new DummyPage<>(missionList, 20);
        when(missionRepository.findAllWithBonusMission(any(Specification.class), any(Pageable.class))).thenReturn(missionPage);
        when(bonusMissionRepository.findAllByMissionId(any())).thenReturn(bonusMissionList);

        // when
        Page<SummarizedMissionDto> list = missionService.list(null, Pageable.unpaged(), null, true);

        // then
        assertThat(list.getTotalElements()).isEqualTo(3);
        for (int i = 0; i < list.getTotalElements(); i++) {
            SummarizedMissionDto dto = list.getContent().get(i);
            assertThat(dto.getId()).isEqualTo(missionList.get(i).getId());
            assertThat(dto.getName()).isEqualTo(missionList.get(i).getName());
            assertThat(dto.getDescription()).isEqualTo(missionList.get(i).getDescription());
            assertThat(dto.getPoint()).isEqualTo(missionList.get(i).getPoint());
            assertThat(dto.getDifficulty()).isEqualTo(messageSource.getMessage("mission.difficulty." + missionList.get(i).getDifficulty().getName().toLowerCase(),
                    null, LocaleContextHolder.getLocale()));
            assertThat(dto.getMissionStatus()).isEqualTo(missionList.get(i).getMissionStatus());
        }
    }

    @Test
    @DisplayName("미션 단일 조회 - 미션이 없을 경우")
    void findOneWithException() {
        // given
        when(missionRepository.findById(any())).thenReturn(java.util.Optional.empty());

        // when
        assertThrows(MissionNotFoundException.class, () -> missionService.findOne(1L));
    }
}