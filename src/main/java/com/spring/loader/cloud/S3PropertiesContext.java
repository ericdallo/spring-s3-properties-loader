package com.spring.loader.cloud;

import static org.springframework.util.ClassUtils.getUserClass;

import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationContext;

import com.amazonaws.services.s3.model.S3Object;
import com.spring.loader.S3PropertiesLocation;
import com.spring.loader.exception.S3ContextRefreshException;

/**
 * Manage the context of properties from S3 
 * 
 * @author Eric Dallo
 * @since 2.1
 * @see S3PropertiesLocation
 */
public class S3PropertiesContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(S3PropertiesContext.class);
	
	private final ApplicationContext applicationContext;
	private final EnvironmentManager environment;
	private final ContextRefresher contextRefresher;
	private final S3Service s3Service;

	public S3PropertiesContext(ApplicationContext applicationContext, EnvironmentManager environment, ContextRefresher contextRefresher, S3Service s3Service) {
		this.applicationContext = applicationContext;
		this.environment = environment;
		this.contextRefresher = contextRefresher;
		this.s3Service = s3Service;
	}

	/**
	 * Allows the feature of refresh beans annotated with {@link RefreshScope} of spring cloud.
     * The annotated beans will be updated with the new properties of the location setted previously 
     * from {@link S3PropertiesLocation#value() }
	 * 
	 * @throws S3ContextRefreshException for any error on refresh properties
	 * @see RefreshScope
	 */
	public void refresh() {
		try {
			Object annotatedBean = applicationContext.getBeansWithAnnotation(S3PropertiesLocation.class).values().iterator().next();
			
			String[] locations = getUserClass(annotatedBean).getAnnotation(S3PropertiesLocation.class).value();
			
			for (String location : locations) {
				
				Properties properties = new Properties();
				S3Object s3Object = s3Service.retriveFrom(location);			
				
				properties.load(s3Object.getObjectContent());
				
				for (Entry<Object, Object> property : properties.entrySet()) {
					environment.setProperty(property.getKey().toString(), property.getValue().toString());
				}
			}
			
			LOGGER.info("Refreshing properties retrieved from S3");
			contextRefresher.refresh();
		} catch (Exception e) {
			throw new S3ContextRefreshException("Could not refresh properties from S3", e);
		}
	}

}
