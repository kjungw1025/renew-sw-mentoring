package com.renew.sw.mentoring.domain.admin.controller.controller;

import com.renew.sw.mentoring.domain.admin.controller.model.dto.request.RequestCreateAdminDto;
import com.renew.sw.mentoring.domain.admin.controller.service.AdminService;
import com.renew.sw.mentoring.domain.excel.MissionParser;
import com.renew.sw.mentoring.domain.excel.TeamParser;
import com.renew.sw.mentoring.domain.excel.dto.RequestMissionExcelDto;
import com.renew.sw.mentoring.domain.excel.dto.RequestTeamExcelDto;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 api (프론트 사용 X)")
public class AdminController {

    private final AdminService adminService;

    private final TeamParser teamParser;
    private final MissionParser missionParser;

    /**
     * 팀 명단 엑셀 파일로 파싱
     *
     * @param dto   엑셀 파일
     */
    @PostMapping(value = "/parse-team", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void parseTeam(@Valid @ModelAttribute RequestTeamExcelDto dto) {
        teamParser.parseTeam(dto.getFile());
    }

    /**
     * 미션 엑셀 파일로 파싱
     *
     * @param dto   엑셀 파일
     */
    @PostMapping(value = "/parse-mission", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void parseMissions(@Valid @ModelAttribute RequestMissionExcelDto dto) {
        missionParser.parseMission(dto.getFile());
    }

    /**
     * 관리자 계정 생성 (프론트X)
     */
    @PostMapping("/create/admin")
    public void createAdmin(@RequestBody RequestCreateAdminDto dto) {
        adminService.createAdmin(dto);
    }
}
