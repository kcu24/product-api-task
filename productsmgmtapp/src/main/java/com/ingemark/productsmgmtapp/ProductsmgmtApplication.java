package com.ingemark.productsmgmtapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = "com.ingemark")
@EnableJpaRepositories(basePackages = "com.ingemark.infranstructure.persistence.repository")
@EntityScan(basePackages = "com.ingemark.infranstructure.persistence.entity")
@EnableCaching
@EnableRetry
public class ProductsmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsmgmtApplication.class, args);
	}

}
