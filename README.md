[![Build Status](https://travis-ci.org/ericdallo/spring-s3-properties-loader.svg?branch=master)](https://travis-ci.org/ericdallo/spring-s3-properties-loader)
# Spring S3 Property Loader
<img align="right"  src="https://raw.githubusercontent.com/ericdallo/spring-s3-properties-loader/images/spring-icon.png?raw=true" width="120" height="120"/>

S3 Property Loader has the aim of allowing loading of Spring property files from S3 bucket, in order to guarantee stateless machine configuration.

Spring PropertyConfigurer replaces standard PropertyConfigurer to load property files from AWS S3 bucket. S3 path could be specified directly into spring beans.

## Install
Gradle:
```groovy
repositories {  
   jcenter()  
}
```
```groovy
compile "com.spring.loader:s3-loader:1.0.4"
```
Maven:
```xml
<dependency>
  <groupId>com.spring.loader</groupId>
  <artifactId>s3-loader</artifactId>
  <version>1.0.4</version>
  <type>pom</type>
</dependency>
```

## How to use

There 2 ways to configure your application to load properties from s3:

**Anotation**

- Adding this annotation to any spring managed bean
```java
@S3PropertiesLocation("my-bucket/my-folder/my-properties.properties")
```
- Using a specific profile to only load properties if the app is running with that profile
```java
@S3PropertiesLocation(path = "my-bucket/my-folder/my-properties.properties", profiles = "production")
```
- Load from a System env variable
```java
@S3PropertiesLocation(path = "${AWS_S3_LOCATION}", profiles = "production")
```
**Configuration**
```java
@Bean
S3PropertyPlaceholderConfigurer s3PropertyPlaceholderConfigurer(AmazonS3 amazonS3) {
    S3PropertyPlaceholderConfigurer s3PropertyPlaceholderConfigurer = new S3PropertyPlaceholderConfigurer(amazonS3);
    s3PropertyPlaceholderConfigurer.setS3Locations("s3://my-bucket/my-folder/my-properties.properties");

    return s3PropertyPlaceholderConfigurer;
}
```

## Requisites

Official spring aws sdk lib.
See: https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-aws
