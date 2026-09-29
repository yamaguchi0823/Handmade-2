package com.example.app.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.SalesChannelDto;
import com.example.app.dto.SalesChannelRequest;
import com.example.app.service.SalesChannelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class SalesChannelApiController {
	
	private final SalesChannelService service;
	
	@GetMapping
	public List<SalesChannelDto> list(){
		return service.list();
	}
	
	@PostMapping
	public void create(
			@RequestBody SalesChannelRequest request
			) {
		service.create(request);
	}
	
	@PutMapping("/{id}")
	public void update(
			@PathVariable Long id,
			@RequestBody SalesChannelRequest request
			) {
		service.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	public void delete(
			@PathVariable Long id
			) {
		service.delete(id);
	}
	
}
