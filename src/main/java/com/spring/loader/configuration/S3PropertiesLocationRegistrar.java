package com.spring.loader.configuration;

import static com.spring.loader.util.WordUtils.classNameloweredCaseFirstLetter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.spring.loader.S3PropertiesLocation;
import com.spring.loader.cloud.S3PropertySource;
import com.spring.loader.util.SystemPropertyResolver;

/**
 * Creates the {@link S3PropertySource} bean.
 * For use with the {@link S3PropertiesLocation} annotation.
 *
 * @author Eric Dallo
 * @since 1.0.3
 * @see S3PropertiesLocation
 * @see S3PropertySource
 */
public class S3PropertiesLocationRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

	private Environment environment;
	private SystemPropertyResolver resolver;

	public S3PropertiesLocationRegistrar() {
		resolver = new SystemPropertyResolver();
	}

	public S3PropertiesLocationRegistrar(Environment environment, SystemPropertyResolver resolver) {
		this.environment = environment;
		this.resolver = resolver;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(S3PropertiesLocation.class.getName()));
		String[] profiles = attributes.getStringArray("profiles");

		if (profiles.length > 0 && !environment.acceptsProfiles(profiles)) {
			return;
		}

		String[] locations = attributes.getStringArray("value");

		String[] formattedLocations = new String[locations.length];

		for (int i = 0; i < locations.length; i++) {
			formattedLocations[i] = resolver.getFormattedValue(locations[i]);
		}

		BeanDefinition configurerDefinition = new RootBeanDefinition(S3PropertiesSourceConfigurer.class);
		configurerDefinition.getPropertyValues().addPropertyValue("s3ResourceLoader", new RuntimeBeanReference("s3ResourceLoader"));
		configurerDefinition.getPropertyValues().add("locations", formattedLocations);

		registry.registerBeanDefinition(classNameloweredCaseFirstLetter(S3PropertiesSourceConfigurer.class), configurerDefinition);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
