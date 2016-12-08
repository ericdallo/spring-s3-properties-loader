package com.spring.loader;

class S3Path {
	
	private final String bucketName;
	private final String keyName;
	
	public S3Path(String bucketName, String keyName) {
		this.bucketName = bucketName;
		this.keyName = keyName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public String getKeyName() {
		return keyName;
	}
	
}