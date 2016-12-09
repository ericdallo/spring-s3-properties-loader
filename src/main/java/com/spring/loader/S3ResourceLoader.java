package com.spring.loader;

import static java.lang.String.format;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

class S3ResourceLoader implements ResourceLoader {

	private static final String S3_PROTOCOL_PREFIX = "s3://";
	private final AmazonS3 s3;

	public S3ResourceLoader(AWSCredentials credentials) {
		this(new AmazonS3Client(credentials));
	}
	
	public S3ResourceLoader(AmazonS3 s3) {
		this.s3 = s3;
	}

	@Override
	public Resource getResource(String location) {
		if (StringUtils.isEmpty(location)) {
			throw new S3ResourceException("Location cannot be empty or null");
		}
		
		if (!location.startsWith(S3_PROTOCOL_PREFIX)) {
			throw new S3ResourceException(format("%s does not starts with '%s'", location, S3_PROTOCOL_PREFIX));
		}
		
		String path = location.substring(S3_PROTOCOL_PREFIX.length(), location.length());
		String bucketName = path.substring(0, path.indexOf('/'));
		String keyName = path.substring(path.indexOf('/') + 1);
		
		try {
			S3Object s3Object = s3.getObject(bucketName, keyName);
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
