package com.example.app.dto;

public record ItemDto(
		Long id,
		String name,
		String description,
		boolean isActive
		) {}
