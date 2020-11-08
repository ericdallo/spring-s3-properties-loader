package com.spring.loader.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Collections;
import java.util.Map;

@TestConfiguration
@ConfigurationProperties("zuul")
public class TestNestedPropertiesYaml {
    private Map<String, Route> routes = Collections.emptyMap();

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private String path;
        private boolean stripPrefix;
        private String url;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return new StringBuilder().append("{ ")
                    .append("path=").append(path)
                    .append(", ")
                    .append("stripPrefix=").append(stripPrefix)
                    .append(", ")
                    .append("url=").append(url)
                    .append(" }").toString();
        }
    }

    @Override
    public String toString() {
        return "{ routes = " + routes + " }";
    }
}
