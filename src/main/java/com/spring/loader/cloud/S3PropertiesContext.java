package com.spring.loader.cloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;

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
	
	private final EnvironmentManager environment;
	private final ContextRefresher contextRefresher;
	private final S3Service s3Service;
	private final S3Path s3Path;

	public S3PropertiesContext(EnvironmentManager environment, ContextRefresher contextRefresher, S3Service s3Service, S3Path s3Path) {
		this.environment = environment;
		this.contextRefresher = contextRefresher;
		this.s3Service = s3Service;
		this.s3Path = s3Path;
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
		try (S3Object s3Object = s3Service.retriveFrom(s3Path.getLocation())){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
		    String line;
	    
			while ((line = reader.readLine()) != null) {
				String[] property = line.split("=");
				environment.setProperty(property[0], property[1]);
			}
			contextRefresher.refresh();
		} catch (Exception e) {
			throw new S3ContextRefreshException("Could not refresh properties in AWS s3", e);
		}
	}

}
