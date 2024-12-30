package com.mehmetvasfi.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Service
public class SyncService {

    public void syncDirectories(String sourceDir, String targetDir) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        Path targetPath = Paths.get(targetDir);

        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException("Source directory does not exist or is not a directory: " + sourceDir);
        }

        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = targetPath.resolve(sourcePath.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = targetPath.resolve(sourcePath.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
