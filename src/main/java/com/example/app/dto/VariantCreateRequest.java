package com.example.app.dto;

import java.math.BigDecimal;

public record VariantCreateRequest(
		Long itemId,
		String skuCode,
		Integer stock,
		String status,
		BigDecimal price
		) {}
