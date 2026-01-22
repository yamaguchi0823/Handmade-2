package com.example.app.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.VariantListRowDto;

// SQLを呼ぶ窓口
@Mapper
public interface VariantMapper {
//検索条件（キーワード、カテゴリ、素材、状態、在庫モード）
	List<VariantListRowDto> search(
			@Param("q") String q,
			@Param("categryId") Long categoryId,
			@Param("materialId") Long materialId,
			@Param("status") String status,
			@Param("stockMode") String stockMode
			);
}
