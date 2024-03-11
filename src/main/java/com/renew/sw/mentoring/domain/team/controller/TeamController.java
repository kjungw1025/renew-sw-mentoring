package com.renew.sw.mentoring.domain.team.controller;

import com.renew.sw.mentoring.domain.completedmission.service.CompletedMissionService;
import com.renew.sw.mentoring.domain.mission.model.dto.list.SummarizedMissionDto;
import com.renew.sw.mentoring.domain.team.model.dto.list.SummarizedTeamDto;
import com.renew.sw.mentoring.domain.team.model.dto.response.ResponsePage;
import com.renew.sw.mentoring.domain.team.model.dto.response.ResponseTeamInfoDto;
import com.renew.sw.mentoring.domain.team.service.TeamService;
import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import com.renew.sw.mentoring.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Tag(name = "멘토멘티 팀", description = "멘토멘티 팀 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;
    private final CompletedMissionService completedMissionService;

    /**
     * 전체 팀 조회
     */
    @GetMapping
    public ResponsePage<SummarizedTeamDto> getTeams(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<SummarizedTeamDto> teams = teamService.getAllTeams(pageable);
        return new ResponsePage<>(teams);
    }

    /**
     * 우리 팀 상세 조회
     * <p>
     *     팀원, 멘토, 총 점수가 조회됩니다.
     * </p>
     */
    @GetMapping("/my")
    @UserAuth
    public ResponseTeamInfoDto myTeam(AppAuthentication auth) {
        return teamService.getMyTeamInfo(auth.getUserId());
    }

    /**
     * 팀별 성공한 미션 상세 조회
     */
    @GetMapping("/success/mission/{teamId}")
    public ResponsePage<SummarizedMissionDto> list(@ParameterObject Pageable pageable,
                                                   @PathVariable Long teamId) {
        Page<SummarizedMissionDto> result = completedMissionService.list(pageable, teamId);
        return new ResponsePage<>(result);
    }

    /**
     * 우리 팀의 성공한 미션 상세 조회
     */
    @GetMapping("/success/mission")
    @UserAuth
    public ResponsePage<SummarizedMissionDto> listMyTeam(AppAuthentication auth,
                                                         @ParameterObject Pageable pageable) {
        Page<SummarizedMissionDto> result = completedMissionService.listMyTeam(pageable, auth.getUserId());
        return new ResponsePage<>(result);
    }

}
