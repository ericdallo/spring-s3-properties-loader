[![Build Status](https://travis-ci.org/ericdallo/spring-s3-properties-loader.svg?branch=master)](https://travis-ci.org/ericdallo/spring-s3-properties-loader)
# Spring S3 Property Loader
<img align="right"  src="https://raw.githubusercontent.com/ericdallo/spring-s3-properties-loader/images/spring-icon.png?raw=true" width="120" height="120"/>

_S3 Property Loader_ has the aim of allowing loading of Spring property files from S3 bucket, in order to guarantee stateless machine configuration.

Spring PropertyConfigurer uses `PropertiesFactoryBean` to load property files from *AWS S3* bucket.

## Install
_Gradle_:
```groovy
repositories {  
   jcenter()  
}
```
```groovy
compile "com.spring.loader:s3-loader:2.2.2"
```
_Maven_:
```xml
<dependency>
  <groupId>com.spring.loader</groupId>
  <artifactId>s3-loader</artifactId>
  <version>2.2.2</version>
  <type>pom</type>
</dependency>
```

## How to use

- Adding this annotation to any spring managed bean
```java
@S3PropertiesLocation("my-bucket/my-folder/my-properties.yaml")
```
- Using a specific profile to only load properties if the app is running with that profile
```java
@S3PropertiesLocation(value = "my-bucket/my-folder/my-properties.properties", profiles = "production")
```
- Load from a System env variable
```java
@S3PropertiesLocation(value = "${AWS_S3_LOCATION}", profiles = "developer")
// or
@S3PropertiesLocation(value = "${AWS_S3_BUCKET}/application/my.properties", profiles = "developer")
```

### Binding properties to a POJO
You can bind the externally loaded properties to a POJO as well.

For e.g., if you have a YAML file as
```yaml
zuul:
  routes:
    query1:
      path: /api/apps/test1/query/**
      stripPrefix: false
      url: "https://test.url.com/query1"
    query2:
      path: /api/apps/test2/query/**
      stripPrefix: false
      url: "https://test.url.com/query2"
    index1:
      path: /api/apps/*/index/**
      stripPrefix: false
      url: "https://test.url.com/index"
```
Then you can bind the properties to a POJO using ConfigurationProperties:
```java
@Component
@ConfigurationProperties("zuul")
public class RouteConfig {
    private Map<String, Map<String, String>> routes = new HashMap<>();

    public void setRoutes(Map<String, Map<String, String>> routes) {
        this.routes = routes;
    }

    public Map<String, Map<String, String>> getRoutes() {
        return routes;
    }
}

// or

@Component
@ConfigurationProperties("zuul")
public class RouteConfig {
    private Map<String, Route> routes;

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private String path;
        private boolean stripPrefix;
        String url;

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
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return this.toString();
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.toString();
    }
}
```

### Refreshing properties in runtime

You can force your application to load properties from S3 again without restart. _S3 Properties Loader_ uses a [Spring Cloud](http://projects.spring.io/spring-cloud/) feature that allows the spring beans annotated with `@RefreshScope` to reload properties.
To work, *it is only necessary* to inject the `S3PropertiesContext` bean and call `refresh()` method. After this, _S3 Properties Loader_ will get properties again from s3 bucket defined previously and refresh your beans annotated with `@RefreshScope`.

_tip_: You can create a endpoint that calls this class and refresh your application via endpoint or create a `@Scheduled` class which updates from time to time.

Example:
```java
@RestController
public SomeController {

   @Autowired
   private S3PropertiesContext s3PropertiesContext;
    
   @PostMapping("/refresh-properties")
   public void refresh() {
       s3PropertiesContext.refresh();
   }
}
```
## Requisites

Official [spring aws sdk lib](https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-aws).

## Problems and Issues

Found some bug? Have some enhancement ? Open a Issue [here](https://github.com/ericdallo/spring-s3-properties-loader/issues)
