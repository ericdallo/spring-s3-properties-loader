package com.spring.loader;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.amazonaws.services.s3.AmazonS3;

@Configuration
class S3ResourseLoaderConfiguration {
	
	@Bean
	@Scope(SCOPE_SINGLETON)
	S3ResourceLoader s3ResourceLoader(AmazonS3 amazonS3) {
		return new S3ResourceLoader(amazonS3);
	}
	
	@Bean
	RefreshProperties refreshProperties(EnvironmentManager environmentManager, ContextRefresher contextRefresher, S3ResourceLoader s3ResourceLoader) {
		return new S3RefreshProperties(environmentManager, contextRefresher, s3ResourceLoader);
	}
}
