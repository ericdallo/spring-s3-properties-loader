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
compile "com.spring.loader:s3-loader:2.0.0"
```
Maven:
```xml
<dependency>
  <groupId>com.spring.loader</groupId>
  <artifactId>s3-loader</artifactId>
  <version>2.0.0</version>
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

## Requisites

Official spring aws sdk lib.
See: https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-aws
