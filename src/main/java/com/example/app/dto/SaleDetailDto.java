package com.example.app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDetailDto(
		Long id,
		LocalDateTime soldAt,
		Long channelId,
		String note,
		List<SaleLineRowDto> lines
		) {}
