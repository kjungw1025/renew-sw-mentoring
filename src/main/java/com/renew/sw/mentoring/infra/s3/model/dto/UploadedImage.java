package com.renew.sw.mentoring.infra.s3.model.dto;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public class UploadedImage {
    private final String fileId;

    private final String originalImageName;

    private final MediaType mimeType;

    private final ImageRequest image;

    public UploadedImage(String fileId, ImageRequest image) {
        this.fileId = fileId;
        this.originalImageName = image.getOriginalImageName();
        this.mimeType = image.getContentType();
        this.image = image;
    }
}
