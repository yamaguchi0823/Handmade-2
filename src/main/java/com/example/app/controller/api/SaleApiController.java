package com.example.app.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.SaleCreateRequest;
import com.example.app.dto.SaleDetailDto;
import com.example.app.dto.SaleListRowDto;
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
	
	@GetMapping
	public List<SaleListRowDto> list(@RequestParam(defaultValue="50") int limit){
		return saleService.listRecent(limit);
	}
	
	@GetMapping("/{saleId}")
	public SaleDetailDto detail(@PathVariable long saleId) {
		return saleService.getDetail(saleId);
	}
	
	
}
