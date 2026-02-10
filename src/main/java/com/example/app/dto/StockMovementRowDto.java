package com.example.app.dto;

import java.time.LocalDateTime;

public record StockMovementRowDto(
		Long id,
		String movementType,
		String delta,
		Integer qtyBefore,
		Integer qtyAfter,
		String refType,
		Long refId,
		String note,
		LocalDateTime createdAt
		) {}
