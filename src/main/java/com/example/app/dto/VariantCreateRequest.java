package com.example.app.dto;

import java.math.BigDecimal;

public record VariantCreateRequest(
		Long itemId,
		String variantName,
		String skuCode,
		Integer stock,
		Integer stockAlertThreshold,
		String status,
		BigDecimal price
		) {}
