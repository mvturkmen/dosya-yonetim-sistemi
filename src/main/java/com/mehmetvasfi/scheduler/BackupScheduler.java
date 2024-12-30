package com.mehmetvasfi.scheduler;

import com.mehmetvasfi.service.FileWatcherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackupScheduler {

    private final FileWatcherService fileWatcherService;

    public BackupScheduler(FileWatcherService fileWatcherService) {
        this.fileWatcherService = fileWatcherService;
    }

    @Scheduled(cron = "0 */2 * * * ?") // Her 2 dakikada bir çalışır
    public void startBackupProcess() {
        System.out.println("Scheduled backup process started...");
        fileWatcherService.backupAllFiles(); // Tüm dosyaları yedekle
    }
}
