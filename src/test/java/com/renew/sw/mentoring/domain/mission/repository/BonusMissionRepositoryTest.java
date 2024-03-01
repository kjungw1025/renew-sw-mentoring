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

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BonusMissionRepositoryTest {

    @Autowired
    private BonusMissionRepository bonusMissionRepository;

    @Autowired
    private MissionRepository missionRepository;

    @BeforeEach
    public void setup() {
        bonusMissionRepository.deleteAll();
        missionRepository.deleteAll();
    }

    @Test
    @DisplayName("미션으로 보너스 미션 조회가 잘 되는지?")
    void findAllByMissionId() {
        // given
        Mission mission = Mission.builder()
                .name("미션")
                .description("미션 설명")
                .point(100)
                .difficulty(Difficulty.HARD)
                .missionStatus(MissionStatus.MAIN)
                .build();
        missionRepository.save(mission);
        BonusMission bm = BonusMission.builder()
                .name("보너스 미션")
                .description("보너스 미션 설명")
                .point(50)
                .mission(mission)
                .build();
        bonusMissionRepository.save(bm);

        // when
        List<BonusMission> bonusMissions = bonusMissionRepository.findAllByMissionId(mission.getId());

        // then
        assertThat(bonusMissions.size()).isEqualTo(1);
        for (BonusMission bonusMission : bonusMissions) {
            assertThat(bonusMission.getName()).isEqualTo("보너스 미션");
            assertThat(bonusMission.getDescription()).isEqualTo("보너스 미션 설명");
            assertThat(bonusMission.getPoint()).isEqualTo(50);
            assertThat(bonusMission.getMission().getId()).isEqualTo(mission.getId());
        }
    }
}
