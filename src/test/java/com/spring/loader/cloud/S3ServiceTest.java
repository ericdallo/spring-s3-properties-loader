package com.spring.loader.cloud;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.spring.loader.exception.InvalidS3LocationException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class S3ServiceTest {
	
	private S3Service subject;

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
	
	@BeforeEach
	public void setup() {
		subject = new S3Service(s3);
	}

	@Test
	public void shouldGetAValidResourceWithPrefix() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.retriveFrom(validLocationWithPrefix);
		
		verify(s3).getObject(anyString(), anyString());
	}
	
	@Test
	public void shouldGetAValidResourceWithoutPrefix() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);
		
		subject.retriveFrom(validLocationWithoutPrefix);
		
		verify(s3).getObject(anyString(), anyString());
	}

	@Test
	public void shouldNotGetAValidResourceWhenLocationIsEmpty() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);

		assertThrows(InvalidS3LocationException.class, () -> subject.retriveFrom(emptyLocation));
		
		verify(s3, never()).getObject(anyString(), anyString());
	}
	
	@Test
	public void shouldNotGetAValidResourceWhenLocationIsInvald() {
		when(s3.getObject(anyString(), anyString())).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(inputStream);

		assertThrows(InvalidS3LocationException.class, () -> subject.retriveFrom(invalidLocation));
		
		verify(s3, never()).getObject(anyString(), anyString());
	}

}
