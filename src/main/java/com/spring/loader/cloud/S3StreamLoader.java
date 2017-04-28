package com.spring.loader.cloud;

import java.io.InputStream;

import com.spring.loader.S3PropertiesLocation;

/**
 * Get {@link InputStream} for the given aws s3 location.
 * For use with the {@link S3PropertiesLocation} annotation.
 *
 * @author Eric Dallo
 * @since 1.0.0
 * @see S3PropertySource
 */
public class S3StreamLoader {

	private final S3Service s3Service;

	public S3StreamLoader(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	public InputStream getProperty(String location) {
		return s3Service.retriveFrom(location).getObjectContent();
	}

}
