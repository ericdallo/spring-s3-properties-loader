package com.spring.loader.cloud;

import static org.springframework.util.StringUtils.isEmpty;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.spring.loader.exception.InvalidS3LocationException;
import com.spring.loader.exception.S3ResourceException;

/**
 * Bridge to {@link AmazonS3} methods
 *
 * @author Eric Dallo
 * @since 2.1
 */
public class S3Service {

	private static final String S3_PROTOCOL_PREFIX = "s3://";
	private final AmazonS3 amazonS3;
	
	public S3Service(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}
	
	/**
	 * @param bucketName + key location
	 * @return {@link S3Object} for the given aws s3 location.
	 * @throws InvalidS3LocationException for invalid location params
	 * @throws S3ResourceException for connection and availability errors
	 */
	public S3Object retriveFrom(String location) {
		if (isEmpty(location)) {
			throw new InvalidS3LocationException("Location cannot be empty or null");
		}

		String path = location.startsWith(S3_PROTOCOL_PREFIX) ? location.substring(S3_PROTOCOL_PREFIX.length(), location.length()) : location;

		if(!path.contains("/")) {
			throw new InvalidS3LocationException("The location must contains the full path of the properties file");
		}

		String bucketName = path.substring(0, path.indexOf('/'));
		String keyName = path.substring(path.indexOf('/') + 1);

		try {
			return amazonS3.getObject(bucketName, keyName);
		} catch (Exception e) {
			throw new S3ResourceException("Could not load resource from " + location, e);
		}
	}
}
