package com.spring.loader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;

@Configuration
class S3ResourseLoaderConfiguration {
	
	@Bean
	S3ResourceLoader s3ResourceLoader(AmazonS3 amazonS3) {
		return new S3ResourceLoader(amazonS3);
	}
}
