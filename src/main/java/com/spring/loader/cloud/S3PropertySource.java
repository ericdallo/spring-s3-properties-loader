package com.spring.loader.cloud;

import java.util.Properties;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;

/**
 * A PropertySource implementation that reads the properties from an S3 bucket.
 * The config file in S3 can be a properties file or YAML file.
 *
 * Most framework-provided {@link PropertySource} implementations are enumerable. Spring
 * does binding of source properties to a Map only for enumerable sources.
 * @see org.springframework.boot.context.properties.bind.MapBinder
 * When possible the SpringIterableConfigurationPropertySource will be used in preference
 * to SpringConfigurationPropertySource since it supports full "relaxed" style resolution.
 */
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
