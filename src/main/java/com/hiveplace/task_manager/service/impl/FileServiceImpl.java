package com.hiveplace.task_manager.service.impl;

import com.hiveplace.task_manager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${tasks.bucket.name}")
    private String bucketName;
    private final S3Client s3Client;

    public FileServiceImpl(S3Client s3Client){
        this.s3Client = s3Client;
    }
    @Override
    public void uploadFile(String fileName, FilePart file) {
        var basePath = Paths.get("").toAbsolutePath();
        var uploadsPath = basePath.resolve(fileName);
        var filePath  = uploadsPath.resolve(file.filename());
        try {
            Files.createDirectories(uploadsPath);
            if (!Files.exists(filePath))
                Files.createFile(filePath);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath.toAbsolutePath()));
           log.info("File uploaded successfully: " + response.eTag());
        } catch (IOException e) {
            log.error("Failed to create directory or file: " + e.getMessage());
        }
    }
}
