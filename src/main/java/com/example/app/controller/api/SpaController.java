package com.example.app.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
	
	@GetMapping({
		"/dashboard",
		"/profit",
		"/inventory",
		"/items",
		"/sales",
		"/channels"
	})
	public String forwardToIndex() {
		return "forward:/index.html";
	}
	

}
