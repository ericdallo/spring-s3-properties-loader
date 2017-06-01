package com.spring.loader.configuration;

import static org.springframework.util.ClassUtils.getUserClass;

import java.util.Arrays;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.spring.loader.S3PropertiesLocation;

/**
 * Check if profile is the same then loads the beans for s3-properties-loader
 *
 * @author Eric Dallo
 * @since 2.2.3
 * @see S3PropertiesLoaderConfiguration
 */
class S3LoaderValidProfileCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Object annotatedBean = context.getBeanFactory().getBeansWithAnnotation(S3PropertiesLocation.class).values().iterator().next();

		String[] profiles = getUserClass(annotatedBean).getAnnotation(S3PropertiesLocation.class).profiles();

		return Arrays.asList(profiles).contains(context.getEnvironment().getActiveProfiles());
	}

}
