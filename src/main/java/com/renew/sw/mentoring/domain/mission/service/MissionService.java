package com.renew.sw.mentoring.domain.mission.service;

import com.renew.sw.mentoring.domain.mission.model.dto.response.ResponseSingleMissionDto;
import com.renew.sw.mentoring.domain.mission.exception.MissionNotFoundException;
import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.dto.list.SummarizedMissionDto;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {

    private final MissionRepository missionRepository;
    private final BonusMissionRepository bonusMissionRepository;

    private final MessageSource messageSource;

    public Page<SummarizedMissionDto> list(String keyword, Pageable pageable, Difficulty difficulty, boolean hasBonusMission) {
        Specification<Mission> spec = MissionSpec.withKeyword(keyword);
        spec = spec.and(MissionSpec.withDifficulty(difficulty));

        Page<Mission> missions;

        if (hasBonusMission) {
            missions = missionRepository.findAllWithBonusMission(spec, pageable);
        } else {
            missions = missionRepository.findAll(spec, pageable);
        }

        return missions.map(mission -> {
            List<BonusMission> bonusMissions = bonusMissionRepository.findAllByMissionId(mission.getId());
            return new SummarizedMissionDto(mission, bonusMissions, messageSource);
        });
    }

    public ResponseSingleMissionDto findOne(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        List<BonusMission> bonusMissions = bonusMissionRepository.findAllByMissionId(missionId);

        return new ResponseSingleMissionDto(mission, bonusMissions, messageSource);
    }
}
