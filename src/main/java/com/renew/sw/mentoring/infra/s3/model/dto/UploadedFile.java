package com.renew.sw.mentoring.infra.s3.model.dto;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public class UploadedFile {
    private final String fileId;

    private final String originalFileName;

    private final MediaType mimeType;

    private final FileRequest file;

    public UploadedFile(String fileId, FileRequest file) {
        this.fileId = fileId;
        this.originalFileName = file.getOriginalFileName();
        this.mimeType = file.getContentType();
        this.file = file;
    }
}
