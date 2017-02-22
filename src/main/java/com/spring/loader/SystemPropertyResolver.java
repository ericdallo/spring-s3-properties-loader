package com.spring.loader;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

import com.spring.loader.exception.EnviromentPropertyNotFoundException;
import com.spring.loader.exception.InvalidS3LocationException;

/**
 * Resolver for properties that will be retrieved from system environment. 
 * 
 * @author Eric Dallo
 * @since 2.0
 */
public class SystemPropertyResolver {

	private static final String SYSTEM_NOTATION_PREFIX = "${";
	private static final String SYSTEM_NOTATION_SUFIX = "}";

	public String getFormattedValue(String value) {
		if (isEmpty(value)) {
			throw new InvalidS3LocationException("The location cannot be empty or null");
		}
		if (value.startsWith(SYSTEM_NOTATION_PREFIX)) {
			
			if (value.endsWith(SYSTEM_NOTATION_SUFIX)) {
				String rawProperty = value.substring(SYSTEM_NOTATION_PREFIX.length(), value.length() - SYSTEM_NOTATION_SUFIX.length());
				String valueFromEnv = System.getenv(rawProperty);
				
				if (isEmpty(valueFromEnv)) {
					throw new EnviromentPropertyNotFoundException(format("Enviroment variable %s not found in system", rawProperty));
				}
				
				return valueFromEnv;
			}
			throw new InvalidS3LocationException("Syntax error for system property: " + value);
		}
		
		return value;
	}

}