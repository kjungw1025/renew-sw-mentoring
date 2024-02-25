package com.renew.sw.mentoring.domain.team.controller;

import com.renew.sw.mentoring.domain.excel.TeamParser;
import com.renew.sw.mentoring.domain.excel.dto.RequestTeamExcelDto;
import com.renew.sw.mentoring.domain.team.service.TeamService;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@Tag(name = "팀", description = "팀 관련 api")
public class TeamController {

    private final TeamService teamService;
}
