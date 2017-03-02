package com.spring.loader;

import java.util.Properties;

import org.springframework.core.env.PropertySource;

class S3PropertySource extends PropertySource<Object> {
	
	private static final String S3_PROPERTY_SOURCE_NAME = "s3PropertySource";

	private final Properties properties;

	S3PropertySource (Properties properties) {
		super(S3_PROPERTY_SOURCE_NAME);
		this.properties = properties;
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name); 
	}
}
