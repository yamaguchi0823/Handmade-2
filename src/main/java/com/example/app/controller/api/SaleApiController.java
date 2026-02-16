package com.example.app.controller.api;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.SaleCreateRequest;
import com.example.app.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleApiController {
	
	private final SaleService saleService;
	
	@PostMapping
	public Map<String,Long> create(
			@RequestBody SaleCreateRequest req
			){
		Long saleId = saleService.createSale(req);
		return Map.of("saleId", saleId);
	}
}
