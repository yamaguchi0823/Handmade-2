package com.example.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.dto.SalesChannelDto;
import com.example.app.mapper.SalesChannelMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesChannelService {
	
	private final SalesChannelMapper mapper;
	
	public List<SalesChannelDto> list(){
		return mapper.selectAll();
	}
	
	public void create(
			String name,
			BigDecimal feeRate,
			BigDecimal fixedFee,
			String note
			) {
		if(name == null || name.isBlank()) {
			throw new IllegalArgumentException("チャネル名は必須です");
		}
		mapper.insert(
				name.trim(), 
				feeRate == null ? BigDecimal.ZERO : feeRate, 
				fixedFee == null ? BigDecimal.ZERO : fixedFee, 
				note
				);
	}
	
	public void update(
			Long id,
			String name,
			BigDecimal feeRate,
			BigDecimal fixedFee,
			String note
			) {
		mapper.update(id, name, feeRate, fixedFee, note);
	}
	
	public void delete(Long id) {
		mapper.delete(id);
	}
}
