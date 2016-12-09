package com.spring.loader;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import com.amazonaws.services.s3.AmazonS3;

public class S3PropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private S3ResourceLoader resourceLoader;
	private List<String> s3Locations = new ArrayList<>();
	private List<Resource> conventionalResources = new ArrayList<>();

	public S3PropertyPlaceholderConfigurer(S3ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	

	public S3PropertyPlaceholderConfigurer(AmazonS3 s3) {
		this(new S3ResourceLoader(s3));
	}

	public void setS3Locations(String...s3Locations) {
		this.s3Locations.addAll(asList(s3Locations));
	}
	
	@Override
	public void setLocations(Resource... locations) {
		this.conventionalResources.addAll(asList(locations));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		int total = conventionalResources.size() + s3Locations.size();

		if (total > 0) {
			List<Resource> allResources = s3Locations.stream()
					.map(resourceLoader::getResource)
					.collect(toList());
			
			conventionalResources.forEach(allResources::add);
			
			super.setLocations(allResources.toArray(new Resource[allResources.size()]));
		}
		
		super.postProcessBeanFactory(beanFactory);
	}
}
