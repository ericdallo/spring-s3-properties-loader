package com.spring.loader;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.spring.loader.exception.InvalidS3LocationException;

/**
 * Get {@link Resource} for the given aws s3 location.
 * For use with the {@link S3PropertiesLocation} annotation. 
 * 
 * @author Eric Dallo
 * @since 1.0.0
 * @see S3PropertyPlaceholderConfigurer
 */
class S3ResourceLoader implements ResourceLoader {

	private static final String S3_PROTOCOL_PREFIX = "s3://";
	private final AmazonS3 amazonS3;

	S3ResourceLoader(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public Resource getResource(String location) {
		if (StringUtils.isEmpty(location)) {
			throw new InvalidS3LocationException("Location cannot be empty or null");
		}
		
		String path = location.startsWith(S3_PROTOCOL_PREFIX) ? location.substring(S3_PROTOCOL_PREFIX.length(), location.length()) : location;
		
		if(!path.contains("/")) {
			throw new InvalidS3LocationException("The location must contains the full path of the properties file");
		}
		
		String bucketName = path.substring(0, path.indexOf('/'));
		String keyName = path.substring(path.indexOf('/') + 1);
		
		try {
			S3Object s3Object = amazonS3.getObject(bucketName, keyName);
			return new InputStreamResource(s3Object.getObjectContent(), location);
		} catch (Exception e) {
			throw new S3ResourceException("Could not load resource from " + location, e);
		}
	}
	
	@Override
	public ClassLoader getClassLoader() {
		return this.getClassLoader();
	}
}
