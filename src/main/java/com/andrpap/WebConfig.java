package com.andrpap;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/gallery/**")
		        .addResourceLocations("file://C://Users//Luffy//eclipse-workspace//bakery-eshop//src//main//resources//gallery//static//images//")
		        .setCachePeriod(0);
	}
}
