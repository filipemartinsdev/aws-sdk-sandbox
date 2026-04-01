package com.aws.cheatsheet.infra.web;

import com.aws.cheatsheet.model.dto.DownloadResponse;
import com.aws.cheatsheet.services.FileService;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/index")
    public ResponseEntity<List<String>> index(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.getIndex());
    }

    @GetMapping("/{key}")
    public ResponseEntity<Resource> download(@PathVariable String key){
        DownloadResponse response = fileService.download(key);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(response.contentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\""
                )
                .body(response.file());
    }

    @GetMapping("/{key}/url")
    public ResponseEntity<Map<String, String>> getFileURL(@PathVariable String key){
        String url = fileService.getFileURL(key);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("url", url));
    }

    @PostMapping
    public ResponseEntity<Void> upload(@RequestBody MultipartFile file){
        fileService.upload(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key){
        fileService.delete(key);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
