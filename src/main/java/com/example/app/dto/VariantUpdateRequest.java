package com.example.app.dto;

import java.math.BigDecimal;

// バリエ更新（編集）
public record VariantUpdateRequest(
		String skuCode,
		String status,
		Integer stockAlertThreshold,
		BigDecimal price
		) {}
