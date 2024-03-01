package com.renew.sw.mentoring.domain.excel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestTeamExcelDto {

    @Schema(description = "팀 엑셀 파일", required = true)
    private final MultipartFile file;

    public RequestTeamExcelDto(MultipartFile file) {
        this.file = file;
    }
}

