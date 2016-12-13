package com.spring.loader.exception;

public class EnviromentPropertyNotFoundException extends S3ResourceException {
	private static final long serialVersionUID = -3434756303367606716L;

	public EnviromentPropertyNotFoundException(String message) {
		super(message);
	}

}
