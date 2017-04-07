package com.spring.loader.cloud;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.spring.loader.S3PropertiesLocation;

/**
 * Get {@link Resource} for the given aws s3 location.
 * For use with the {@link S3PropertiesLocation} annotation.
 *
 * @author Eric Dallo
 * @since 1.0.0
 * @see S3PropertySource
 */
public class S3ResourceLoader implements ResourceLoader {

	private final S3Service s3Service;

	public S3ResourceLoader(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	@Override
	public Resource getResource(String location) {
		return new InputStreamResource(s3Service.retriveFrom(location).getObjectContent(), location);
	}

	@Override
	public ClassLoader getClassLoader() {
		return this.getClassLoader();
	}

}
