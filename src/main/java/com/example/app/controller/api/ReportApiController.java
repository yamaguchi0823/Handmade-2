package com.example.app.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.ChannelProfitRowDto;
import com.example.app.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportApiController {
	
	private final SaleService saleService;
	
	@GetMapping("/channel-profit")
	public List<ChannelProfitRowDto> channelProfit(
			@RequestParam(required = false) LocalDate from,
			@RequestParam(required = false) LocalDate to
			){
		return saleService.channelProfit(from, to);
	}
	

}
