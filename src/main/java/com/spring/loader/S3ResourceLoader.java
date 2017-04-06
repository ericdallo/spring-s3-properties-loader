package com.spring.loader;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.spring.loader.exception.InvalidS3LocationException;
import com.spring.loader.exception.S3ResourceException;

/**
 * Get {@link Resource} for the given aws s3 location.
 * For use with the {@link S3PropertiesLocation} annotation. 
 * 
 * @author Eric Dallo
 * @since 1.0.0
 * @see S3PropertySource
 */
public class S3ResourceLoader implements ResourceLoader {

	private static final String S3_PROTOCOL_PREFIX = "s3://";
	private final AmazonS3 amazonS3;
	private String location;

	S3ResourceLoader(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public Resource getResource(String location) {
		return new InputStreamResource(retriveFromS3(location).getObjectContent(), location);
	}
	
	public S3ObjectInputStream getS3Object(String location) {
		return retriveFromS3(location).getObjectContent();
	}
	
	private S3Object retriveFromS3(String location) {
		if (StringUtils.isEmpty(location)) {
			throw new InvalidS3LocationException("Location cannot be empty or null");
		}
		
		String path = location.startsWith(S3_PROTOCOL_PREFIX) ? location.substring(S3_PROTOCOL_PREFIX.length(), location.length()) : location;
		
		if(!path.contains("/")) {
			throw new InvalidS3LocationException("The location must contains the full path of the properties file");
		}
		
		this.location = location;
		
		String bucketName = path.substring(0, path.indexOf('/'));
		String keyName = path.substring(path.indexOf('/') + 1);
		
		try {
			return amazonS3.getObject(bucketName, keyName);
		} catch (Exception e) {
			throw new S3ResourceException("Could not load resource from " + location, e);
		}
	}

	@Override
	public ClassLoader getClassLoader() {
		return this.getClassLoader();
	}

	public InputStream getForSameLocation() {
		return retriveFromS3(location).getObjectContent();
	}
}
