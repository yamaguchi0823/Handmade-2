package com.example.app.dto;

import java.math.BigDecimal;

public record SaleLineRowDto(
		Long id,
		Long variantId,
		String skuCode,
		String itemName,
		Integer qty,
		BigDecimal unitPrice,
		BigDecimal costPrice,
		BigDecimal lineAmount
		) {}
