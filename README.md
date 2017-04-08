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
compile "com.spring.loader:s3-loader:2.2.1"
```
_Maven_:
```xml
<dependency>
  <groupId>com.spring.loader</groupId>
  <artifactId>s3-loader</artifactId>
  <version>2.2.1</version>
  <type>pom</type>
</dependency>
```

## How to use

- Adding this annotation to any spring managed bean
```java
@S3PropertiesLocation("my-bucket/my-folder/my-properties.properties")
```
- Using a specific profile to only load properties if the app is running with that profile
```java
@S3PropertiesLocation(value = "my-bucket/my-folder/my-properties.properties", profiles = "production")
```
- Load from a System env variable
```java
@S3PropertiesLocation(value = "${AWS_S3_LOCATION}", profiles = "developer")
or
@S3PropertiesLocation(value = "${AWS_S3_BUCKET}/application/my.properties", profiles = "developer")
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
