package com.spring.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

public class S3PropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private S3ResourceLoader resourceLoader;
	private String[] s3Locations = new String[0];
	private Resource[] conventionalResources = new Resource[0];

	public S3PropertyPlaceholderConfigurer(S3ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setLocations(Resource... locations) {
		this.conventionalResources = locations;
	}

	@SuppressWarnings("deprecation")
	public void setS3Locations(String[] s3Locations) {
		this.s3Locations = new String[s3Locations.length];
		for (int i = 0; i < s3Locations.length; i++) {
			this.s3Locations[i] = parseStringValue(s3Locations[i], new Properties(), new HashSet<>());
		}

	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		injectS3Resources();
		super.postProcessBeanFactory(beanFactory);
	}

	private void injectS3Resources() {
		int total = conventionalResources.length + s3Locations.length;

		if (total > 0) {
			List<Resource> allResources = new ArrayList<>();
			for (String s3Location : s3Locations) {
				allResources.add(resourceLoader.getResource(s3Location));
			}
			for (Resource conventionalResource : conventionalResources) {
				allResources.add(conventionalResource);
			}

			super.setLocations(allResources.toArray(new Resource[0]));
		}

	}
}
