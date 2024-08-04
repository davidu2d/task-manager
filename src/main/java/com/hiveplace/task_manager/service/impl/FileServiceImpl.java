package com.hiveplace.task_manager.service.impl;

import com.hiveplace.task_manager.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Value("${tasks.bucket.name}")
    private String bucketName;
    private final S3Client s3Client;

    public FileServiceImpl(S3Client s3Client){
        this.s3Client = s3Client;
    }
    @Override
    public void uploadFile(String fileName, FilePart file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromByteBuffer(file.content().map(dataBuffer -> {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(dataBuffer.readableByteCount());
                            dataBuffer.read(byteBuffer.array());
                            byteBuffer.flip();
                            return byteBuffer;
                        }).blockFirst()
                    )
                );
    }
}
