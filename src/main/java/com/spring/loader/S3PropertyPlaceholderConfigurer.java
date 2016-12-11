package com.spring.loader;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import com.amazonaws.services.s3.AmazonS3;

/**
 * Get S3 properties and add in spring context through {@link PropertyPlaceholderConfigurer} spring bean.
 * 
 * It's possible to auto configure this bean with the {@link S3PropertiesLocation} annotation. 
 * 
 * @author Eric Dallo
 * @since 1.0.0
 * @see S3PropertiesLocation
 */
public class S3PropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private S3ResourceLoader s3ResourceLoader;
	private List<String> s3Locations = new ArrayList<>();
	private List<Resource> conventionalResources = new ArrayList<>();

	@Deprecated // Spring eyes only
	S3PropertyPlaceholderConfigurer() {
	}

	public S3PropertyPlaceholderConfigurer(S3ResourceLoader s3ResourceLoader) {
		this.s3ResourceLoader = s3ResourceLoader;
	}

	public S3PropertyPlaceholderConfigurer(AmazonS3 s3) {
		this(new S3ResourceLoader(s3));
	}

	public void setS3ResourceLoader(S3ResourceLoader s3ResourceLoader) {
		this.s3ResourceLoader = s3ResourceLoader;
	}

	public void setS3Locations(String... s3Locations) {
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
			List<Resource> allResources = new ArrayList<>();
			for (String location : s3Locations) {
				allResources.add(s3ResourceLoader.getResource(location));
			}

			allResources.addAll(conventionalResources);

			super.setLocations(allResources.toArray(new Resource[allResources.size()]));
		}

		super.postProcessBeanFactory(beanFactory);
	}
}
