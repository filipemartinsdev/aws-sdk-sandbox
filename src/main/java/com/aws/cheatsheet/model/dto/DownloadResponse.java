package com.aws.cheatsheet.model.dto;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public record DownloadResponse(
        String contentType,
        Resource file
) {
}
