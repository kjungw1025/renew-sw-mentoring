package com.renew.sw.mentoring.domain.excel.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestMissionExcelDto {

    private final MultipartFile file;

    public RequestMissionExcelDto(MultipartFile file) {
        this.file = file;
    }
}
