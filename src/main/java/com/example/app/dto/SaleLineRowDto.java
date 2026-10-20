package com.example.app.dto;

import java.math.BigDecimal;

public record SaleLineRowDto(
		Long id,
		Long itemId,
		Long variantId,
		String skuCode,
		String itemName,
		String variantName,
		Integer qty,
		BigDecimal unitPrice,
		BigDecimal costPrice,
		BigDecimal lineAmount
		) {}
