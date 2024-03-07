package com.renew.sw.mentoring.domain.mission.controller;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.dto.list.SummarizedMissionDto;
import com.renew.sw.mentoring.domain.mission.model.dto.response.ResponseSingleMissionDto;
import com.renew.sw.mentoring.domain.mission.service.MissionService;
import com.renew.sw.mentoring.global.auth.role.UserAuth;
import com.renew.sw.mentoring.global.model.dto.ResponsePage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "미션", description = "미션 관련 API")
@RequestMapping("/mission")
public class MissionController {

    private final MissionService missionService;

    /**
     * 미션 전체 조회
     *
     * @param keyword          검색어
     * @param pageable         페이징
     * @param hasBonusMission  보너스 미션 포함 여부
     * @param difficulty       난이도
     */
    @GetMapping
    public ResponsePage<SummarizedMissionDto> list(@RequestParam(required = false) String keyword,
                                                   @ParameterObject Pageable pageable,
                                                   @RequestParam(required = false) boolean hasBonusMission,
                                                   @RequestParam(required = false) Difficulty difficulty
                                                   ) {
        Page<SummarizedMissionDto> result = missionService.list(keyword, pageable, difficulty, hasBonusMission);
        return new ResponsePage<>(result);
    }

    /**
     * 미션 상세 조회
     *
     * @param missionId   미션 id
     */
    @GetMapping("/{missionId}")
    public ResponseSingleMissionDto findOne(@PathVariable Long missionId) {
        return missionService.findOne(missionId);
    }
}
