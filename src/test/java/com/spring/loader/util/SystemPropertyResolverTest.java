package com.spring.loader.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.spring.loader.exception.EnviromentPropertyNotFoundException;
import com.spring.loader.exception.InvalidS3LocationException;

@PrepareForTest(SystemPropertyResolver.class)
@RunWith(PowerMockRunner.class)
public class SystemPropertyResolverTest {

	private SystemPropertyResolver subject;

	@Before
	public void setup() {
		subject = new SystemPropertyResolver();
		PowerMockito.mockStatic(System.class);
	}

	@Test
	public void shouldGetFormattedValueWhenValueIsValid() {
		String expected = "someValue";

		PowerMockito.when(System.getenv(eq("AWS_S3"))).thenReturn(expected);

		String env = "${AWS_S3}";

		String formattedValue = subject.getFormattedValue(env);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldGetFormattedValueFromPropertiesWhenValueIsValid() {
		String expected = "someValue";

		PowerMockito.when(System.getProperty(eq("AWS_S3"))).thenReturn(expected);

		String env = "${AWS_S3}";

		String formattedValue = subject.getFormattedValue(env);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldGetCombinedFormattedValueWhenValueIsValid() {
		String region = "someRegion";
		String environment = "someEnvironment";

		PowerMockito.when(System.getenv(eq("S3_BUCKET_REGION"))).thenReturn(region);
		PowerMockito.when(System.getenv(eq("S3_BUCKET_ENVIRONMENT"))).thenReturn(environment);

		String configValue = "${S3_BUCKET_REGION}/${S3_BUCKET_ENVIRONMENT}/myApplication/application.properties";
		String expected = String.format("%s/%s/myApplication/application.properties", region, environment);

		String formattedValue = subject.getFormattedValue(configValue);

		assertEquals(expected, formattedValue);
	}

	@Test
	public void shouldReplaceMultiple() {
		String environment = "dev";

		PowerMockito.when(System.getenv(eq("EC2_ENVIRONMENT"))).thenReturn(environment);

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

	@Test(expected = InvalidS3LocationException.class)
	public void shouldNotGetFormattedValueWhenValueIsEmpty() {
		String expected = "";

		subject.getFormattedValue(expected);
	}

	@Test(expected = InvalidS3LocationException.class)
	public void shouldNotGetFormattedValueWhenValueHasAInvalidSyntax() {
		String expected = "${AWS_S3";

		subject.getFormattedValue(expected);
	}

	@Test(expected = EnviromentPropertyNotFoundException.class)
	public void shouldNotGetFormattedValueWhenSystemDoesNotHasTheEnvironmentVariable() {
		PowerMockito.when(System.getenv(eq("AWS_S3"))).thenReturn(null);

		String env = "${AWS_S3}";

		subject.getFormattedValue(env);
	}

}