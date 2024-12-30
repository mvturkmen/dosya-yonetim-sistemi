package com.mehmetvasfi.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.mehmetvasfi.config.FileStorageProperties;

@SpringBootApplication
@EntityScan(basePackages = { "com.mehmetvasfi" })
@ComponentScan(basePackages = { "com.mehmetvasfi" })
@EnableJpaRepositories(basePackages = { "com.mehmetvasfi" })
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class DosyaYoneticisiRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosyaYoneticisiRestApiApplication.class, args);
	}

}
