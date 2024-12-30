package com.mehmetvasfi.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mehmetvasfi.config.FileStorageProperties;
import com.mehmetvasfi.service.FileWatcherService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = { "com.mehmetvasfi" })
@ComponentScan(basePackages = { "com.mehmetvasfi" })
@EnableJpaRepositories(basePackages = { "com.mehmetvasfi" })
@EnableConfigurationProperties({
		FileStorageProperties.class
})
@EnableScheduling
public class DosyaYoneticisiRestApiApplication {

	@Autowired
	private FileWatcherService fileWatcherService;

	public static void main(String[] args) {
		SpringApplication.run(DosyaYoneticisiRestApiApplication.class, args);
	}

	@PostConstruct
	public void startFileWatcher() {
		Thread fileWatcherThread = new Thread(fileWatcherService);
		fileWatcherThread.start();
	}
}
