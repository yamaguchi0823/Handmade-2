package com.example.app.dto;

import java.math.BigDecimal;

public record ChannelProfitRowDto(
		Long channelId,
		String channelName,
		Integer salesCount,
		Integer linesCount,
		Integer toralQty,
		BigDecimal totalAmount,
		BigDecimal totalCost,
		BigDecimal feeAmount,
		BigDecimal fixedAmount,
		BigDecimal profit
		) {}
