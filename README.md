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
compile "com.spring.loader:s3-loader:1.0.2"
```
Maven:
```xml
<dependency>
  <groupId>com.spring.loader</groupId>
  <artifactId>s3-loader</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```
## How to use

Declare a spring bean `S3PropertyPlaceholderConfigurer` using yours AWS credencials or AWS instance profile. e.i: 

```java
@Bean
S3PropertyPlaceholderConfigurer s3PropertyPlaceholderConfigurer(AmazonS3 s3) {
    S3PropertyPlaceholderConfigurer s3PropertyPlaceholderConfigurer = new S3PropertyPlaceholderConfigurer(s3);
    s3PropertyPlaceholderConfigurer.setS3Locations("s3://my-bucket/my-folder/my-properties.properties");

    return s3PropertyPlaceholderConfigurer;
}
```
