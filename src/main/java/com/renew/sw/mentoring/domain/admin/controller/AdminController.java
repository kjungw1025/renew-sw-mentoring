package com.renew.sw.mentoring.domain.admin.controller;

import com.renew.sw.mentoring.domain.admin.request.RequestCreateAdminDto;
import com.renew.sw.mentoring.domain.admin.service.AdminService;
import com.renew.sw.mentoring.domain.excel.MissionParser;
import com.renew.sw.mentoring.domain.excel.TeamParser;
import com.renew.sw.mentoring.domain.excel.dto.RequestMissionExcelDto;
import com.renew.sw.mentoring.domain.excel.dto.RequestTeamExcelDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedMissionBoardDto;
import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import com.renew.sw.mentoring.global.model.dto.ResponsePage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 미승인된 글 전체 조회 (관리자용)
     */
    @GetMapping("/unapproved/mission")
    @AdminAuth
    public ResponsePage<SummarizedMissionBoardDto> unapprovedList(AppAuthentication auth,
                                                                  @RequestParam(defaultValue = "10") int bodySize,
                                                                  @ParameterObject Pageable pageable) {
        Page<SummarizedMissionBoardDto> unapprovedList = adminService.unapprovedList(auth.getUserRole(), pageable, bodySize);
        return new ResponsePage<>(unapprovedList);
    }
}
