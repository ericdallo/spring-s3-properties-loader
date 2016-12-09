package com.spring.loader;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@RunWith(MockitoJUnitRunner.class)
public class S3ResourceLoaderTest {
	
	private S3ResourceLoader subject;

	@Mock
	private AmazonS3 s3;
	@Mock
	private S3Object s3Object;
	@Mock
	private S3ObjectInputStream inputStream;

	private String validLocation = "s3://mybucket/myfolder";
	private String invalidLocation = "mybucket/myfolder";
	private String emptyLocation = "";
	
	@Before
	public void setup() {
		subject = new S3ResourceLoader(s3);
	}

	@Test
	public void shouldGetAValidResource() {
		when(s3.getObject(Mockito.anyString(), Mockito.anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(validLocation);
		
		verify(s3).getObject(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test(expected = S3ResourceException.class)
	public void shouldNotGetAValidResourceWhenLocationIsEmpty() {
		when(s3.getObject(Mockito.anyString(), Mockito.anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(emptyLocation);
		
		verify(s3, never()).getObject(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test(expected = S3ResourceException.class)
	public void shouldNotGetAValidResourceWhenLocationIsInvald() {
		when(s3.getObject(Mockito.anyString(), Mockito.anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.getResource(invalidLocation);
		
		verify(s3, never()).getObject(Mockito.anyString(), Mockito.anyString());
	}

}
