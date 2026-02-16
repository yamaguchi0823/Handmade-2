package com.example.app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SaleCreateRequest(
		LocalDateTime soldAt,
		Long channelId,
		String note,
		List<SaleLineCreateRequest> lines
		) {
}
