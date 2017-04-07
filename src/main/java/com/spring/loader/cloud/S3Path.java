package com.spring.loader.cloud;

/**
 * Pojo for location of the S3 bucket + key
 *
 * @author Eric Dallo
 * @since 2.1
 */
public class S3Path {

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
