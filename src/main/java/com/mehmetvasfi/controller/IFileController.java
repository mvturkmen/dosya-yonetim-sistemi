package com.mehmetvasfi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.mehmetvasfi.dto.ShareFileRequest;
import com.mehmetvasfi.model.FileEntity;

public interface IFileController {

    public String uploadFile(MultipartFile[] files, String username);

    public List<FileEntity> getFilesByUser(String username);

    public ResponseEntity<String> shareFile(ShareFileRequest request);
}
