package com.hiveplace.task_manager.service;

import org.springframework.http.codec.multipart.FilePart;

import java.io.IOException;

public interface FileService {
    void uploadFile(String fileName, FilePart file) throws IOException;
}
