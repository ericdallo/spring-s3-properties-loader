package com.spring.loader.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@ConfigurationProperties("test")
public class BasicTestConfigPojo {
    String property1;
    String property2;

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{ ")
                .append("property1=").append(property1)
                .append(", ")
                .append("property2=").append(property2)
                .append(" }").toString();
    }
}
