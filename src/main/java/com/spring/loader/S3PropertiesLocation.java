package com.spring.loader;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Allow the auto configuration of the {@link S3PropertyPlaceholderConfigurer} bean.
 * 
 * @author Eric Dallo
 * @since 1.0.3
 * @see S3PropertiesLocationConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({S3ResourseLoaderConfiguration.class, S3PropertiesLocationConfiguration.class})
@Documented
public @interface S3PropertiesLocation {
	
	/**
	 * The location of the properties in aws s3.
	 * 
	 * @return the path of aws s3 properties, e.g. my-bucket/my-folder/app.properties  
	 */
	String[] value();
	
}
