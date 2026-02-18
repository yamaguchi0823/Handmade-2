package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleListRowDto(
		Long id,
		LocalDateTime soldAt,
		Long channelId,
		String note,
		Integer linesCount,
		Integer totalQty,
		BigDecimal totalAmount
		) {}
