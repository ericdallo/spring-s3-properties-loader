package com.spring.loader.exception;

public class S3ContextRefreshException extends RuntimeException {
	
	private static final long serialVersionUID = 7836301699281948051L;

	public S3ContextRefreshException(String message, Exception e) {
		super(message, e);
	}

}
