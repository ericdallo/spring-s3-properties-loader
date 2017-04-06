package com.spring.loader.configuration;

import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.spring.loader.cloud.S3Path;
import com.spring.loader.cloud.S3PropertiesContext;
import com.spring.loader.cloud.S3ResourceLoader;
import com.spring.loader.cloud.S3Service;

@Configuration
public class S3PropertiesLoaderConfiguration {

	@Bean
	S3Service s3Service(AmazonS3 amazonS3) {
		return new S3Service(amazonS3);
	}
	
	@Bean
	S3ResourceLoader s3ResourceLoader(S3Service s3Service) {
		return new S3ResourceLoader(s3Service);
	}

	@Bean
	S3PropertiesContext refreshProperties(EnvironmentManager environmentManager, ContextRefresher contextRefresher, S3Service s3Service, S3Path s3Path) {
		return new S3PropertiesContext(environmentManager, contextRefresher, s3Service, s3Path);
	}
}
