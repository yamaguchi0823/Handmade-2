package com.example.app.dto;

import java.math.BigDecimal;

public record SaleLineCreateRequest(
		Long variantId,
		Integer qty,
		BigDecimal unitPrice, // nullならDBのvariant.priceを使う
		BigDecimal costPrice  // nullならDBのvariant.cost_priceを使う
		) {}
