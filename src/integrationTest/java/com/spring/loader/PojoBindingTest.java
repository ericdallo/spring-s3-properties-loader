package com.spring.loader;

import cloud.localstack.awssdkv1.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.s3.AmazonS3;
import com.spring.loader.configuration.BasicTestConfigPojo;
import com.spring.loader.configuration.SpringBootTestApplication;
import com.spring.loader.configuration.TestNestedPropertiesYaml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { "s3" })
@Import( { S3Config.class, BasicTestConfigPojo.class, TestNestedPropertiesYaml.class })
@S3PropertiesLocation( { "integration-test/basic-props.properties", "integration-test/test-nested-properties.yml" } )
@SpringBootTest (classes = { SpringBootTestApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PojoBindingTest {

    @Autowired
    AmazonS3 amazonS3Client;

    @Autowired
    TestNestedPropertiesYaml testNestedPropertiesYaml;

    @Autowired
    BasicTestConfigPojo basicTestConfigPojo;

    @Value("${test.property1}")
    String testProp1;

    @BeforeAll
    public static void init() {
        AmazonS3 s3 = TestUtils.getClientS3();

        s3.createBucket("integration-test");
        s3.putObject("integration-test", "test-nested-properties.yml",
                Paths.get("./src/integrationTest/resources/test-nested-properties.yml").toFile());
        s3.putObject("integration-test", "basic-props.properties",
                Paths.get("./src/integrationTest/resources/basic-props.properties").toFile());
    }

    @Test
    public void testS3PropertiesAreBoundedToPojo() {
        assertEquals("value1", testProp1);
        assertEquals("value1", basicTestConfigPojo.getProperty1());
        assertEquals("value2", basicTestConfigPojo.getProperty2());

        assertEquals(testNestedPropertiesYaml.getRoutes().size(), 3);
        assertNotNull(testNestedPropertiesYaml.getRoutes().get("index"));
        assertNotNull(testNestedPropertiesYaml.getRoutes().get("query1"));
        assertNotNull(testNestedPropertiesYaml.getRoutes().get("query2"));
        assertTrue(testNestedPropertiesYaml.getRoutes().get("index").isStripPrefix());
        assertEquals("https://test.url.com/query1", testNestedPropertiesYaml.getRoutes().get("query1").getUrl());
        assertEquals("/api/apps/test2/query/**", testNestedPropertiesYaml.getRoutes().get("query2").getPath());
    }
}

@TestConfiguration
class S3Config {

    @Primary
    @Bean
    public AmazonS3 amazonS3Client() {
        return TestUtils.getClientS3();
    }
}
