package com.spring.loader.cloud;

import java.util.Properties;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.lang.Nullable;

public class S3PropertySource extends EnumerablePropertySource<Object> {
	
	private static final String S3_PROPERTY_SOURCE_NAME = "s3PropertySource";

	private final Properties properties;

	public S3PropertySource (Properties properties) {
		super(S3_PROPERTY_SOURCE_NAME);
		this.properties = properties;
	}

	@Override
	@Nullable
	public Object getProperty(String name) {
		return this.properties.get(name);
	}

	@Override
	public boolean containsProperty(String name) {
		return this.properties.containsKey(name);
	}

	@Override
	public String[] getPropertyNames() {
		return this.properties.keySet().toArray(new String[this.properties.size()]);
	}
}
