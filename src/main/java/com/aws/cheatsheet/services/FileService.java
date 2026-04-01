package com.aws.cheatsheet.services;

import com.aws.cheatsheet.model.dto.DownloadResponse;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
public class FileService {
    private final S3Template s3Template;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String BUCKET;

    public FileService(S3Template s3Template, S3Presigner s3Presigner) {
        this.s3Template = s3Template;
        this.s3Presigner = s3Presigner;
    }

    public List<String> getIndex(){
        return s3Template.listObjects(BUCKET, "").stream()
                .map(resource -> resource.getFilename())
                .toList();
    }

    public DownloadResponse download(String key){
        S3Resource response = s3Template.download(BUCKET, key);
        try {
            return new DownloadResponse(
                    response.contentType(),
                    new InputStreamResource(response.getInputStream())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upload(MultipartFile multipartFile){
        try {
            s3Template.upload(BUCKET, multipartFile.getOriginalFilename(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key){
        s3Template.deleteObject(BUCKET, key);
    }

    public String getFileURL(String key){
        return s3Template.createSignedGetURL(BUCKET, key, Duration.ofMinutes(5)).toString();
    }
}
