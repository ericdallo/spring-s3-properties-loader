package com.spring.loader.configuration;

import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.spring.loader.cloud.S3PropertiesContext;
import com.spring.loader.cloud.S3StreamLoader;
import com.spring.loader.cloud.S3Service;

@Configuration
public class S3PropertiesLoaderConfiguration {

	@Bean
	S3Service s3Service(AmazonS3 amazonS3) {
		return new S3Service(amazonS3);
	}
	
	@Bean
	S3StreamLoader s3ResourceLoader(S3Service s3Service) {
		return new S3StreamLoader(s3Service);
	}

	@Bean
	S3PropertiesContext refreshProperties(ApplicationContext applicationContext, EnvironmentManager environmentManager, ContextRefresher contextRefresher, S3Service s3Service) {
		return new S3PropertiesContext(applicationContext, environmentManager, contextRefresher, s3Service);
	}
}
