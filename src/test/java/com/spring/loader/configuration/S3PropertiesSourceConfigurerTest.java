package com.spring.loader.configuration;

import com.spring.loader.cloud.S3StreamLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3PropertiesSourceConfigurerTest {

    @Mock
    private S3StreamLoader s3StreamLoader;

    private S3PropertiesSourceConfigurer s3PropertiesSourceConfigurer;

    @BeforeEach
    public void setup() {

        when(s3StreamLoader.getProperty("external-config.properties"))
                .thenReturn(getClass().getClassLoader().getResourceAsStream("external-config.properties"));
        when(s3StreamLoader.getProperty("external-config.yaml"))
                .thenReturn(getClass().getClassLoader().getResourceAsStream("external-config.yaml"));

        s3PropertiesSourceConfigurer = new S3PropertiesSourceConfigurer();
        s3PropertiesSourceConfigurer.setS3ResourceLoader(s3StreamLoader);
    }

    @Test
    public void shouldParsePropertiesFileAndYamlFileIntoSameProperties() {
        Properties properties = s3PropertiesSourceConfigurer.loadProperties("external-config.properties");
        Properties yamlProperties = s3PropertiesSourceConfigurer.loadProperties("external-config.yaml");

        System.out.println(properties);
        System.out.println(yamlProperties);

        assertEquals(properties.size(), yamlProperties.size());

        yamlProperties.keySet().forEach((key) ->
                assertEquals(yamlProperties.getProperty(key.toString()), properties.getProperty(key.toString())));
    }
}
