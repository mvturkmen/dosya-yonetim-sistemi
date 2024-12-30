package com.mehmetvasfi.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileWatcherService implements Runnable {

    private final Path watchPath;
    private final Path backupPath;
    private final ExecutorService executorService;

    public FileWatcherService(
            @Value("${watch.directory}") String watchDir,
            @Value("${backup.directory:C:\\\\Users\\\\mvturkmen\\\\Desktop\\\\spring_boot_ws\\\\dosya-yoneticisi-rest-api\\\\backup}") String backupDir) {
        this.watchPath = Paths.get(watchDir);
        this.backupPath = Paths.get(backupDir);
        this.executorService = Executors.newFixedThreadPool(4); // Yedekleme işlemleri için thread havuzu
    }

    @Override
    public void run() {
        System.out.println("Backing up all files from upload directory...");
        backupAllFiles(); // Uygulama başlatıldığında mevcut dosyaları yedekle

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind: " + event.kind() + ". File: " + event.context());

                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE
                            || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path sourceFilePath = watchPath.resolve((Path) event.context());
                        Path destinationFilePath = backupPath.resolve((Path) event.context());
                        backupFile(sourceFilePath, destinationFilePath);
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public void backupAllFiles() {
        try {
            Files.walk(watchPath).filter(Files::isRegularFile).forEach(file -> {
                Path destinationFilePath = backupPath.resolve(watchPath.relativize(file));
                backupFile(file, destinationFilePath);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to back up all files from " + watchPath);
        }
    }

    private void backupFile(Path source, Path destination) {
        executorService.submit(() -> {
            try {
                Files.createDirectories(destination.getParent()); // Hedef dizin yoksa oluştur
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING); // Dosyayı yedekle
                System.out.println("File backed up: " + source + " -> " + destination);
            } catch (IOException e) {
                System.err.println("Failed to back up file: " + source);
                e.printStackTrace();
            }
        });
    }
}
