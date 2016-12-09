package com.spring.loader;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@RunWith(MockitoJUnitRunner.class)
public class S3PropertyPlaceholderConfigurerTest {

	private String location = "s3://somebucket/somefolder";

	private S3PropertyPlaceholderConfigurer subject;
	private S3ResourceLoader resourceLoader;
	
	@Mock
	private AmazonS3 s3;
	@Mock
	private ConfigurableListableBeanFactory beanFactory;
	@Mock
	private Resource resource;
	@Mock
	private S3Object s3Object;
	@Mock
	private S3ObjectInputStream s3ObjectInputStream;

	@Before
	public void setup() {
		resourceLoader = new S3ResourceLoader(s3);
		subject = new S3PropertyPlaceholderConfigurer(resourceLoader);
		
		when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{});
	}
	
	@Test
	public void shouldProcessDefaultProperties() {
		subject.postProcessBeanFactory(beanFactory);
		
		verify(s3Object, never()).getObjectContent();
	}
	
	@Test
	public void shouldProcessS3Properties() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
		
		subject.setS3Locations(location);
		subject.postProcessBeanFactory(beanFactory);
		
		verify(s3Object).getObjectContent();
	}

}
