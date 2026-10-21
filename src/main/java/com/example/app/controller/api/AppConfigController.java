package com.example.app.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppConfigController {
	
	@Value("${app.demo-read-only:false}")
	private boolean readOnly;
	
	@GetMapping("/api/app-config")
	public Map<String, Boolean> getAppConfig() {
		return Map.of("readOnly", readOnly);
	}

}
