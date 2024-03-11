package com.renew.sw.mentoring.domain.completedmission.service;

import com.renew.sw.mentoring.domain.completedmission.model.entity.CompletedMission;
import com.renew.sw.mentoring.domain.completedmission.repository.CompletedMissionRepository;
import com.renew.sw.mentoring.domain.mission.exception.MissionNotFoundException;
import com.renew.sw.mentoring.domain.mission.model.dto.list.SummarizedMissionDto;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompletedMissionService {

    private final CompletedMissionRepository completedMissionRepository;
    private final MissionRepository missionRepository;
    private final BonusMissionRepository bonusMissionRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    /**
     * 팀별 성공한 미션 상세 조회
     */
    public Page<SummarizedMissionDto> list(Pageable pageable, Long teamId) {
        Page<CompletedMission> completedMissions;

        completedMissions = completedMissionRepository.findAllByTeamId(pageable, teamId);

        return completedMissions.map(completedMission -> {
            List<BonusMission> bonusMissions = new ArrayList<>();

            Mission mission = missionRepository.findById(completedMission.getMission().getId()).orElseThrow(MissionNotFoundException::new);
            if (completedMission.isBonusSuccess()) {
                bonusMissions = bonusMissionRepository.findAllByMissionId(completedMission.getMission().getId());
            }
            return new SummarizedMissionDto(mission, bonusMissions, messageSource);
        });
    }

    /**
     * 우리 팀의 성공한 미션 상세 조회
     */
    public Page<SummarizedMissionDto> listMyTeam(Pageable pageable, Long userId) {
        Page<CompletedMission> completedMissions;

        Long teamId = userRepository.findById(userId).orElseThrow(UserNotFoundException::new).getId();
        completedMissions = completedMissionRepository.findAllByTeamId(pageable, teamId);

        return completedMissions.map(completedMission -> {
            List<BonusMission> bonusMissions = new ArrayList<>();

            Mission mission = missionRepository.findById(completedMission.getMission().getId()).orElseThrow(MissionNotFoundException::new);
            if (completedMission.isBonusSuccess()) {
                bonusMissions = bonusMissionRepository.findAllByMissionId(completedMission.getMission().getId());
            }
            return new SummarizedMissionDto(mission, bonusMissions, messageSource);
        });
    }
}
