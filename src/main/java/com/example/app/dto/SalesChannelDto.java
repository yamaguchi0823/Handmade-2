package com.example.app.dto;

import java.math.BigDecimal;

public record SalesChannelDto(
		Long id,
		String name,
		BigDecimal feeRate,
		BigDecimal fixedFee,
		String note
		) {}
