package com.sample.reonwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
"com.sample.utils",
"com.sample.controllers",
"com.sample.repositories",
"com.sample.models"
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ReonwebsiteApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ReonwebsiteApplication.class);
		
	}
	public static void main(String[] args) {
		SpringApplication.run(ReonwebsiteApplication.class, args);
	}

}
