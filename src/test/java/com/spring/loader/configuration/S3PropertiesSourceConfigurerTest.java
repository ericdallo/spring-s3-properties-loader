package com.spring.loader.configuration;

import com.spring.loader.cloud.S3StreamLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class S3PropertiesSourceConfigurerTest {

    @Mock
    private S3StreamLoader s3StreamLoader;

    private S3PropertiesSourceConfigurer s3PropertiesSourceConfigurer;

    @Before
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
                assertEquals("Value mismatch for key '" + key + "';",
                        yamlProperties.getProperty(key.toString()), properties.getProperty(key.toString())));
    }
}
