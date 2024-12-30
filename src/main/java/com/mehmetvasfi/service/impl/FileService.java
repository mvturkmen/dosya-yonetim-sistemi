package com.mehmetvasfi.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mehmetvasfi.model.FileEntity;
import com.mehmetvasfi.model.User;
import com.mehmetvasfi.repository.FileRepository;
import com.mehmetvasfi.repository.UserRepository;
import com.mehmetvasfi.service.IFileService;

import jakarta.annotation.PostConstruct;

@Service
public class FileService implements IFileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @PostConstruct
    public void init() {
        if (uploadDirectory == null || uploadDirectory.isEmpty()) {
            throw new RuntimeException("Upload directory is not configured properly!");
        }
        Path path = Paths.get(uploadDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory!", e);
            }
        }
    }

    public List<FileEntity> saveFiles(MultipartFile[] files, Integer userId) throws IOException {
        List<FileEntity> savedFiles = new ArrayList<>();

        // Kullanıcıyı bul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory + File.separator + fileName;

            // Save file to filesystem
            File dest = new File(filePath);
            file.transferTo(dest);

            // Save file info to database
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(filePath);
            fileEntity.setUser(user);
            fileEntity.setUploadDate(LocalDateTime.now());

            savedFiles.add(fileRepository.save(fileEntity));
        }

        return savedFiles;
    }

    public List<FileEntity> getFilesByUserId(Integer userId) {
        return fileRepository.findByUser_Id(userId);
    }

    @Override
    public FileEntity uploadFile(MultipartFile file, Integer user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
    }

    @Override
    public List<FileEntity> getFilesByUser(Integer user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFilesByUser'");
    }

    @Override
    public void shareFile(Integer fileId, String username) {
        // Kullanıcıyı bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Dosyayı bul
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found with ID: " + fileId));

        // Kullanıcının erişim listesine dosyayı ekle
        if (!user.getAccessibleFiles().contains(file)) {
            user.getAccessibleFiles().add(file);
            userRepository.save(user); // Değişiklikleri kaydet
        } else {
            throw new IllegalArgumentException("File is already shared with this user.");
        }
    }

}
