package com.mehmetvasfi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mehmetvasfi.repository.FileOperationRepository;
import com.mehmetvasfi.service.BackupService;
import com.mehmetvasfi.service.LoggingService;
import com.mehmetvasfi.service.SyncService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/rest/api/file-operations")
@CrossOrigin(origins = "*")
public class FileOperationsController {

    private final BackupService backupService;
    private final SyncService syncService;
    private final LoggingService loggingService;
    private final FileOperationRepository fileOperationRepository;

    @Autowired
    public FileOperationsController(BackupService backupService,
            SyncService syncService,
            LoggingService loggingService,
            FileOperationRepository fileOperationRepository) {
        this.backupService = backupService;
        this.syncService = syncService;
        this.loggingService = loggingService;
        this.fileOperationRepository = fileOperationRepository;
    }

    @GetMapping("/backup")
    public ResponseEntity<String> backupFile(@RequestParam String sourcePath, @RequestParam String backupDir) {
        try {
            String decodedSourcePath = URLDecoder.decode(sourcePath, StandardCharsets.UTF_8.toString());
            String decodedBackupDir = URLDecoder.decode(backupDir, StandardCharsets.UTF_8.toString());

            Path source = Paths.get(decodedSourcePath);
            Path backup = Paths.get(decodedBackupDir);

            backupService.backupFile(source, backup);
            loggingService.logToFile("backup", "Backup completed for: " + decodedSourcePath);
            return ResponseEntity.ok("Backup successful");
        } catch (Exception e) {
            loggingService.logToFile("error", "Backup failed for: " + sourcePath + " - Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Backup failed: " + e.getMessage());
        }
    }

    @GetMapping("/sync")
    public ResponseEntity<String> syncDirectories(@RequestParam String sourceDir, @RequestParam String targetDir) {
        try {
            String decodedSourceDir = URLDecoder.decode(sourceDir, StandardCharsets.UTF_8.toString());
            String decodedTargetDir = URLDecoder.decode(targetDir, StandardCharsets.UTF_8.toString());

            syncService.syncDirectories(decodedSourceDir, decodedTargetDir);
            loggingService.logToFile("sync",
                    "Synchronization completed between " + decodedSourceDir + " and " + decodedTargetDir);
            return ResponseEntity.ok("Synchronization successful");
        } catch (Exception e) {
            loggingService.logToFile("error", "Synchronization failed - Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Synchronization failed: " + e.getMessage());
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs(@RequestParam String category) {
        try {
            List<String> logs = fileOperationRepository.getLogsByCategory(category);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of("Failed to retrieve logs: " + e.getMessage()));
        }
    }
}