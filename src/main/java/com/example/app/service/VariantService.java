package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.dto.VariantListRowDto;
import com.example.app.mapper.VariantMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VariantService {
	
	private final VariantMapper variantMapper;
	
	// 検索条件（キーワード、カテゴリ、素材、状態、在庫モード）
	public List<VariantListRowDto> search(
			String q,
			Long categoryId,
			Long materialId,
			String status,
			String stockMode
			){
		return variantMapper.search(
				q, categoryId, materialId, status, stockMode
				);
	}
}
