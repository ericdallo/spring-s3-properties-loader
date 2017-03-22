package com.spring.loader;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

import com.spring.loader.exception.EnviromentPropertyNotFoundException;
import com.spring.loader.exception.InvalidS3LocationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolver for properties that will be retrieved from system environment. 
 * 
 * @author Eric Dallo
 * @since 2.0
 */
public class SystemPropertyResolver {

	private static final String SYSTEM_NOTATION_PREFIX = "${";

	public String getFormattedValue(String value) {
		if (isEmpty(value)) {
			throw new InvalidS3LocationException("The location cannot be empty or null");
		}

		if (value.contains(SYSTEM_NOTATION_PREFIX)) {
			String bucket = value;
			String pattern = "\\$\\{([A-Za-z0-9_\\-\\.]+)\\}";
			Matcher matcher = Pattern.compile(pattern).matcher(bucket);
			while (matcher.find()) {
				String envValue = getFromEnv(matcher.group(1));
				Pattern subExpression = Pattern.compile(Pattern.quote(matcher.group(0)));
				bucket = subExpression.matcher(bucket).replaceAll(envValue);
			}

			if (bucket.contains(SYSTEM_NOTATION_PREFIX)) {
				throw new InvalidS3LocationException("Syntax error for system property: " + value);
			}

			return bucket;
		}
		
		return value;
	}

	private String getFromEnv(String key) {
		String valueFromEnv = System.getenv(key);

		if (isEmpty(valueFromEnv)) {
			throw new EnviromentPropertyNotFoundException(format("Environment variable %s not found in system", key));
		}

		return valueFromEnv;
	}
}