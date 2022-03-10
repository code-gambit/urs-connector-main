package com.urlshortner.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UrlShortenerApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(UrlShortenerApplication.class, args);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
