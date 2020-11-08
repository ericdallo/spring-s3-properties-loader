package com.spring.loader.configuration;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.spring.loader.S3PropertiesLocation;
import com.spring.loader.cloud.S3PropertySource;
import com.spring.loader.cloud.S3StreamLoader;
import org.springframework.core.io.InputStreamResource;

/**
 * Add a new {@link PropertySource} to spring property sources from a S3 bucket
 * For use with the {@link S3PropertiesLocation} annotation.
 *
 * @author Eric Dallo
 * @since 2.0
 * @see S3PropertiesLocation
 * @see S3PropertySource
 */
public class S3PropertiesSourceConfigurer implements EnvironmentAware, BeanFactoryPostProcessor, PriorityOrdered {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3PropertiesSourceConfigurer.class);

	private Environment environment;
	private S3StreamLoader s3ResourceLoader;
	private String[] locations;

	public void setS3ResourceLoader(S3StreamLoader s3ResourceLoader) {
		this.s3ResourceLoader = s3ResourceLoader;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
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
	public void postProcessBeanFactory(@SuppressWarnings("unused") ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (this.environment instanceof ConfigurableEnvironment) {

			PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
			MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();

			propertiesFactory.setSingleton(false);

			Properties[] propertiesToAdd = new Properties[locations.length];

			LOGGER.info("Starting to load properties from S3 into application");

			for (int i = 0; i < locations.length; i++) {
				Properties properties = loadProperties(locations[i]);
				propertiesToAdd[i] = properties;

				for (Entry<Object, Object> entry : properties.entrySet()) {
					LOGGER.debug("Loading property '{}={}'", entry.getKey(), entry.getValue());
				}
			}

			propertiesFactory.setPropertiesArray(propertiesToAdd);

			try {
				propertiesFactory.afterPropertiesSet();
				propertySources.addFirst(new S3PropertySource(propertiesFactory.getObject()));

				LOGGER.info("Successfully loaded properties from S3 into application");
			} catch (IOException e) {
				LOGGER.error("Could not read properties from s3Location", e);
			}

		} else {
			LOGGER.warn("Environment is not of type '{}' property source with instance data is not available", ConfigurableEnvironment.class.getName());
		}
	}

	Properties loadProperties(String s3File) {
		String extn = getFileExtension(s3File);
		switch(extn) {
			case "yaml":
			case "yml":
				var yamlBean = new YamlPropertiesFactoryBean();
				yamlBean.setSingleton(false);
				yamlBean.afterPropertiesSet();
				yamlBean.setResources(new InputStreamResource(s3ResourceLoader.getProperty(s3File)));
				return yamlBean.getObject();
			case "properties":
			default:
				Properties properties = new Properties();
				try {
					properties.load(s3ResourceLoader.getProperty(s3File));
				} catch (IOException e) {
					LOGGER.error("Could not load properties from location " + s3File, e);
				} catch (Exception e) {
					LOGGER.error("Error on loading properties from location: " + s3File, e);
				}
				return properties;
		}
	}

	private String getFileExtension(String filename) {
		return Optional.of(filename.toLowerCase())
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1))
				.get();
	}
}
