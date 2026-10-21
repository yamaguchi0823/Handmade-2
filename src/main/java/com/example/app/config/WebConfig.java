package com.example.app.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final DemoReadOnlyInterceptor demoReadOnlyInterceptor;

	public WebConfig(DemoReadOnlyInterceptor demoReadOnlyInterceptor) {
	    this.demoReadOnlyInterceptor = demoReadOnlyInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(demoReadOnlyInterceptor)
	            .addPathPatterns("/api/**");
	}
	
	@Value("${app.upload-dir}")
	private String uploadDir;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
		.addMapping("/dpi/**")
		.allowedOrigins("http://localhost:5173")
		.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(dir);
		} catch(IOException e) {
			throw new IllegalStateException(
					"画像保存フォルダを作成できません：" + dir, e
					);			
		}
		
		registry.addResourceHandler("/uploads/**")
			.addResourceLocations(dir.toUri().toString());
	}

}
