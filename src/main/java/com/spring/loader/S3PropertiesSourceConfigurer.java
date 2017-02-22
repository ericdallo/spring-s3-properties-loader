package com.spring.loader;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

/**
 * Add a new {@link PropertySource} to spring property sources from a S3 bucket 
 * For use with the {@link S3PropertiesLocation} annotation. 
 * 
 * @author Eric Dallo
 * @since 2.0
 * @see S3PropertiesLocation
 * @see S3PropertySource
 */
class S3PropertiesSourceConfigurer implements EnvironmentAware, BeanFactoryPostProcessor, PriorityOrdered {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3PropertiesSourceConfigurer.class);
	
	private Environment environment;
	private S3ResourceLoader s3ResourceLoader;
	private String[] s3Locations;

	public void setS3ResourceLoader(S3ResourceLoader s3ResourceLoader) {
		this.s3ResourceLoader = s3ResourceLoader;
	}
	
	public void setS3Locations(String[] s3Locations) {
		this.s3Locations = s3Locations;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (this.environment instanceof ConfigurableEnvironment) {
			
			PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
			MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();
			
			for (String s3Location : s3Locations) {
				
				propertiesFactory.setLocation(s3ResourceLoader.getResource(s3Location));
				try {
					propertiesFactory.afterPropertiesSet();
					propertySources.addFirst(new S3PropertySource(propertiesFactory.getObject()));
				} catch (IOException e) {
					LOGGER.error("Could not read properties from s3Location: " + s3Location, e);
				}
			}
			
		} else {
			LOGGER.warn("Environment is not of type '{}' property source with instance data is not available", ConfigurableEnvironment.class.getName());
		}
	}

}
