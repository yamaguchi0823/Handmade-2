package com.example.app.dto;

import java.math.BigDecimal;

public record SalesChannelRequest(
		String name,
		BigDecimal feeRate,
		BigDecimal fixedFee,
		String note
		) {
	
}