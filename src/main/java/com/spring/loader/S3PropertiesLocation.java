package com.spring.loader;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Allow the auto configuration of the {@link S3PropertySource} bean.
 * 
 * @author Eric Dallo
 * @since 1.0.3
 * @see S3PropertiesLocationRegistrar
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ S3ResourseLoaderConfiguration.class, S3PropertiesLocationRegistrar.class })
@Documented
public @interface S3PropertiesLocation {

	/**
	 * The location of the properties in aws s3.
	 * 
	 * @return the path of aws s3 properties                       e.g. "my-bucket/my-folder/app.properties" 
	 *         or a enviroment system to the s3 path               e.g. "${MY_BUCKET_IN_AWS_S3}"
	 */
	String[] value();

	/**
	 * The profiles to load the properties in aws s3.
	 * 
	 * @return the profile name e.g. "prod"
	 */
	String[] profiles() default {};
}
