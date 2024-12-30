package com.mehmetvasfi.controller.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mehmetvasfi.controller.IFileController;
import com.mehmetvasfi.dto.ShareFileRequest;
import com.mehmetvasfi.model.FileEntity;
import com.mehmetvasfi.model.User;
import com.mehmetvasfi.repository.FileRepository;
import com.mehmetvasfi.repository.UserRepository;
import com.mehmetvasfi.service.impl.FileService;

@RestController
@RequestMapping("/rest/api/files")
public class FileController implements IFileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    /**
     * Upload multiple files and associate them with a user.
     *
     * @param files  Files to be uploaded
     * @param userId User ID to associate files with
     * @return List of saved files or error message
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("userId") Integer userId) {
        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No files provided for upload.");
            }

            List<FileEntity> savedFiles = fileService.saveFiles(files, userId);
            return ResponseEntity.ok(savedFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading files: " + e.getMessage());
        }
    }

    /**
     * Get all files uploaded by a specific user.
     *
     * @param userId User ID
     * @return List of user's files or error message
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserFiles(@PathVariable Integer userId) {
        try {
            List<FileEntity> files = fileService.getFilesByUserId(userId);
            if (files.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No files found for user with ID: " + userId);
            }
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving files: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile[] files, String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
    }

    @Override
    @GetMapping("/users/{username}")
    public List<FileEntity> getFilesByUser(@PathVariable("username") String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı bulunamadı: " + username);
        }

        List<FileEntity> files = fileRepository.findByUser_Id(user.get().getId());

        if (files.isEmpty()) {
            throw new NoSuchElementException("Kullanıcı için dosya bulunamadı: " + username);
        }

        return files;
    }

    @Override
    @PostMapping("/share")
    public ResponseEntity<String> shareFile(@RequestBody ShareFileRequest request) {
        try {
            fileService.shareFile(request.getFileId(), request.getUsername());
            return ResponseEntity.ok("File shared successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while sharing the file.");
        }
    }

}
