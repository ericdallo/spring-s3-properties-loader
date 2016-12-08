package com.spring.loader;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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
	public ClassLoader getClassLoader() {
		return this.getClassLoader();
	}

	@Override
	public Resource getResource(String location) {
		try {
			S3Path s3Path = parseS3Path(location);
			S3Object s3Object = s3.getObject(s3Path.getBucketName(), s3Path.getKeyName());
			return new InputStreamResource(s3Object.getObjectContent(), location);

		} catch (Exception e) {
			throw new S3ResourceException("Could not load resource from " + location, e);
		}
	}

	private static S3Path parseS3Path(String location) {

		String path = getLocationPath(location);
		String bucketName = path.substring(0, path.indexOf("/"));
		String keyName = path.substring(path.indexOf("/") + 1);

		return new S3Path(bucketName, keyName);
	}

	private static String getLocationPath(String location) {

		if (location == null || "".equals(location.trim())) {
			throw new S3ResourceException("Location cannot be empty or null");
		}

		if (location.startsWith(S3_PROTOCOL_PREFIX)) {
			return location.substring(S3_PROTOCOL_PREFIX.length(), location.length());
		}

		throw new S3ResourceException(location + " does not begin with '" + S3_PROTOCOL_PREFIX + "'");
	}

}
