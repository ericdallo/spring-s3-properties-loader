package com.spring.loader.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.spring.loader.exception.EnviromentPropertyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.spring.loader.exception.InvalidS3LocationException;

@ExtendWith(MockitoExtension.class)
public class SystemPropertyResolverTest {

	@Spy
	SystemPropertyResolver subject = new SystemPropertyResolver();

	@Test
	public void shouldGetFormattedValueWhenValueIsValid() {
		String expected = "someValue";

		when(subject.getFromEnv("AWS_S3")).thenReturn(expected);

		String env = "${AWS_S3}";

		String formattedValue = subject.getFormattedValue(env);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldGetFormattedValueFromPropertiesWhenValueIsValid() {
		String expected = "someValue";

		when(subject.getFromEnv("AWS_S3")).thenReturn(expected);

		String env = "${AWS_S3}";

		String formattedValue = subject.getFormattedValue(env);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldGetCombinedFormattedValueWhenValueIsValid() {
		String region = "someRegion";
		String environment = "someEnvironment";

		when(subject.getFromEnv("S3_BUCKET_REGION")).thenReturn(region);
		when(subject.getFromEnv("S3_BUCKET_ENVIRONMENT")).thenReturn(environment);

		String configValue = "${S3_BUCKET_REGION}/${S3_BUCKET_ENVIRONMENT}/myApplication/application.properties";
		String expected = String.format("%s/%s/myApplication/application.properties", region, environment);

		String formattedValue = subject.getFormattedValue(configValue);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldReplaceMultiple() {
		String environment = "dev";

		when(subject.getFromEnv("EC2_ENVIRONMENT")).thenReturn(environment);

		String configValue = "region-${EC2_ENVIRONMENT}/deploy-${EC2_ENVIRONMENT}/application.properties";
		String expected = String.format("region-%s/deploy-%s/application.properties", environment, environment);

		String formattedValue = subject.getFormattedValue(configValue);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldGetFormattedValueWhenValueIsValidAndNotASystemEnv() {
		String expected = "someValue";

		String formattedValue = subject.getFormattedValue(expected);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldNotGetFormattedValueWhenValueIsEmpty() {
		String expected = "";

		assertThrows(InvalidS3LocationException.class, () -> subject.getFormattedValue(expected));
	}

	@Test
	public void shouldNotGetFormattedValueWhenValueHasAInvalidSyntax() {
		String expected = "${AWS_S3";

		assertThrows(InvalidS3LocationException.class, () -> subject.getFormattedValue(expected));
	}

	@Test
	public void shouldNotGetFormattedValueWhenSystemDoesNotHasTheEnvironmentVariable() {
		when(subject.getFromEnv(eq("AWS_S3"))).thenReturn(null);

		String env = "${AWS_S3}";

		assertThrows(EnviromentPropertyNotFoundException.class, () -> subject.getFormattedValue(env));
	}

}