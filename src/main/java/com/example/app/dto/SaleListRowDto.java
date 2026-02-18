package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleListRowDto(
		Long id,
		LocalDateTime soldAt,
		Long channelId,
		
		String channelName,
		BigDecimal feeRate,
		BigDecimal fixedFee,
		
		String note,
		Integer linesCount,
		Integer totalQty,
		BigDecimal totalAmount,
		
		BigDecimal totalCost,
		BigDecimal feeAmount,
		BigDecimal profit
		
		) {}
