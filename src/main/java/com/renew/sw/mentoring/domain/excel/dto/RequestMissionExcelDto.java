package com.renew.sw.mentoring.domain.excel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestMissionExcelDto {

    @Schema(description = "미션 엑셀 파일", required = true)
    private final MultipartFile file;

    public RequestMissionExcelDto(MultipartFile file) {
        this.file = file;
    }
}
