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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.spring.loader.exception.InvalidS3LocationException;

@RunWith(MockitoJUnitRunner.class)
public class S3ResourceLoaderTest {
	
	private S3ResourceLoader subject;

	@Mock
	private AmazonS3 s3;
	@Mock
	private S3Object s3Object;
	@Mock
	private S3ObjectInputStream inputStream;

	private String validLocationWithPrefix = "s3://mybucket/myfolder";
	private String validLocationWithoutPrefix = "mybucket/myfolder";
	private String invalidLocation = "mybucket";
	private String emptyLocation = "";
	
	@Before
	public void setup() {
		subject = new S3ResourceLoader(s3);
	}

	@Test
	public void shouldGetAValidResourceWithPrefix() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(validLocationWithPrefix);
		
		verify(s3).getObject(anyString(), anyString());
	}
	
	@Test
	public void shouldGetAValidResourceWithoutPrefix() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(validLocationWithoutPrefix);
		
		verify(s3).getObject(anyString(), anyString());
	}
	
	@Test(expected = InvalidS3LocationException.class)
	public void shouldNotGetAValidResourceWhenLocationIsEmpty() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(emptyLocation);
		
		verify(s3, never()).getObject(anyString(), anyString());
	}
	
	@Test(expected = InvalidS3LocationException.class)
	public void shouldNotGetAValidResourceWhenLocationIsInvald() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(invalidLocation);
		
		verify(s3, never()).getObject(anyString(), anyString());
	}

}
