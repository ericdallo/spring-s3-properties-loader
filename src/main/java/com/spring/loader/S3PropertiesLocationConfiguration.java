package com.spring.loader;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Creates the {@link S3PropertyPlaceholderConfigurer} bean.
 * For use with the {@link S3PropertiesLocation} annotation. 
 * 
 * @author Eric Dallo
 * @since 1.0.3
 * @see S3PropertiesLocation
 * @see S3PropertyPlaceholderConfigurer
 */
class S3PropertiesLocationConfiguration implements ImportBeanDefinitionRegistrar {
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		BeanDefinition bd = new RootBeanDefinition(S3PropertyPlaceholderConfigurer.class);
		
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(S3PropertiesLocation.class.getName()));
		String[] locations = attributes.getStringArray("value");
		
		MutablePropertyValues properties = bd.getPropertyValues();
		
		properties.addPropertyValue("s3ResourceLoader", new RuntimeBeanReference("s3ResourceLoader")); 
		
		properties.add("s3Locations", locations);
		
		registry.registerBeanDefinition("s3PropertyPlaceholderConfigurer", bd);
	}

}
