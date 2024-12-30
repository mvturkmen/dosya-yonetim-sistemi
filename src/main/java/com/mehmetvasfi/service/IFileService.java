package com.mehmetvasfi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mehmetvasfi.model.FileEntity;

public interface IFileService {
    public FileEntity uploadFile(MultipartFile file, Integer user_id);

    public List<FileEntity> getFilesByUser(Integer user_id);
}
