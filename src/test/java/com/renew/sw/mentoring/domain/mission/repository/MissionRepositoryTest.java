package com.renew.sw.mentoring.domain.mission.repository;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private BonusMissionRepository bonusMissionRepository;

    @BeforeEach
    void setup() {
        bonusMissionRepository.deleteAll();
        missionRepository.deleteAll();

        for (int i = 0; i < 3; i++) {
            Mission mission = Mission.builder()
                    .name("미션" + i)
                    .description("미션 설명" + i)
                    .missionStatus(MissionStatus.MAIN)
                    .difficulty(Difficulty.NORMAL)
                    .point(30)
                    .build();
            missionRepository.save(mission);
            BonusMission bm = BonusMission.builder()
                    .name("보너스 미션" + i)
                    .description("보너스 미션 설명" + i)
                    .mission(mission)
                    .point(10)
                    .build();
            bonusMissionRepository.save(bm);
        }

        for (int i = 0; i < 2; i++) {
            Mission mission2 = Mission.builder()
                    .name("노미션" + i)
                    .description("노미션 설명" + i)
                    .missionStatus(MissionStatus.MAIN)
                    .difficulty(Difficulty.NORMAL)
                    .point(30)
                    .build();
            missionRepository.save(mission2);
        }
    }

    @Test
    @DisplayName("미션 전체 조회가 잘 되는지?")
    void findAll() {
        // given & when
        Page<Mission> missions = missionRepository.findAll((root, query, criteriaBuilder) -> null, Pageable.unpaged());

        // then
        assertThat(missions.getTotalElements()).isEqualTo(5);
    }

    @Test
    @DisplayName("보너스 미션을 포함한 미션 전체 조회가 잘 되는지?")
    void findAllWithBonusMission() {
        // given & when
        Page<Mission> missions = missionRepository.findAllWithBonusMission((root, query, criteriaBuilder) -> null, Pageable.unpaged());

        // then
        assertThat(missions.getTotalElements()).isEqualTo(3);
    }
}
