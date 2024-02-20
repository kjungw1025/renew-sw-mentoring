package com.renew.sw.mentoring.domain.mission.controller;

import com.renew.sw.mentoring.domain.excel.MissionParser;
import com.renew.sw.mentoring.domain.excel.dto.RequestMissionExcelDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "미션", description = "미션 관련 API")
@RequestMapping("/mission")
public class MissionController {

    private final MissionParser missionParser;

    /**
     * 미션 엑셀 파일 파싱 후 저장
     */
    @PostMapping(value = "/parsing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void parsing(@Valid @ModelAttribute RequestMissionExcelDto dto) throws Exception {
        missionParser.parseMission(dto.getFile());
    }
}
