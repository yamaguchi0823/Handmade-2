package com.example.app.controller.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.SalesChannelDto;
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
	public void create(@RequestBody Map<String,Object> body) {
		service.create(
				(String) body.get("name"),
				body.get("feeRate") == null ? null : new BigDecimal(body.get("feeRate").toString()),
				body.get("fixedFee")== null ? null : new BigDecimal(body.get("fixedFee").toString()),
				(String) body.get("note")
				);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
}
