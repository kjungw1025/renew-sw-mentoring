package com.renew.sw.mentoring.domain.team.controller;

import com.renew.sw.mentoring.domain.team.service.TeamService;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    /**
     * 멘토멘티 팀 생성
     * 
     * @param teamName 팀명
     */
    @PostMapping("/create")
    @AdminAuth
    public void createTeam(@RequestParam("teamName") String teamName) {
        teamService.createTeam(teamName);
    }
}
