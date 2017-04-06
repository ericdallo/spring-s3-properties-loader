package com.spring.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;

public class S3RefreshProperties implements RefreshProperties {
	
	private final EnvironmentManager environment;
	private final ContextRefresher contextRefresher;
	private final S3ResourceLoader s3ResourceLoader;
	
	public S3RefreshProperties(EnvironmentManager environment, ContextRefresher contextRefresher, S3ResourceLoader s3ResourceLoader) {
		this.environment = environment;
		this.contextRefresher = contextRefresher;
		this.s3ResourceLoader = s3ResourceLoader;
	}

	@Override
	public void refresh() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(s3ResourceLoader.getForSameLocation()));
	    String line;
	    try {
			while((line = reader.readLine()) != null) {
				String[] property = line.split("=");
				environment.setProperty(property[0], property[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		contextRefresher.refresh();
	}
	
}
