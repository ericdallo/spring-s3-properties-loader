package com.spring.loader;

import static java.lang.Character.toLowerCase;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
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
class S3PropertiesLocationRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {
	
	private Environment environment;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(S3PropertiesLocation.class.getName()));
		String[] profiles = attributes.getStringArray("profiles");
		
		if (profiles.length > 0 && !environment.acceptsProfiles(profiles)) {
			return;
		}
		
		String[] locations = attributes.getStringArray("value");
		
		BeanDefinition bd = new RootBeanDefinition(S3PropertyPlaceholderConfigurer.class);
		
		bd.getPropertyValues().addPropertyValue("s3ResourceLoader", new RuntimeBeanReference("s3ResourceLoader")); 
		bd.getPropertyValues().add("s3Locations", locations);
		
		String beanName = S3PropertyPlaceholderConfigurer.class.getSimpleName();
		registry.registerBeanDefinition(toLowerCase(beanName.charAt(0)) + beanName.substring(1), bd);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
