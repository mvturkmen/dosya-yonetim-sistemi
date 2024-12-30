package com.mehmetvasfi.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileWatcherService implements Runnable {

    private final Path watchPath;

    public FileWatcherService(@Value("${watch.directory}") String watchDir) {
        this.watchPath = Paths.get(watchDir);
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind: " + event.kind() + ". File: " + event.context());
                    // İlgili işlemi burada başlat
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
