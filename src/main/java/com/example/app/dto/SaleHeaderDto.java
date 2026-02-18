package com.example.app.dto;

import java.time.LocalDateTime;

public record SaleHeaderDto(
		Long id,
		LocalDateTime soldAt,
		Long channelId,
		String note
		) {}
