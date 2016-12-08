package com.spring.loader;

public class S3ResourceException extends RuntimeException {
	private static final long serialVersionUID = 8310280589629514933L;

	public S3ResourceException(String msg) {
		super(msg);
	}

	public S3ResourceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
