package com.example.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.dto.SalesChannelDto;
import com.example.app.dto.SalesChannelRequest;
import com.example.app.mapper.SalesChannelMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesChannelService {

	private static final BigDecimal MAX_FEE_RATE =
			new BigDecimal("100");

	private static final BigDecimal MAX_FIXED_FEE =
			new BigDecimal("9999999999.99");

	private final SalesChannelMapper mapper;

	public List<SalesChannelDto> list() {
		return mapper.selectAll();
	}

	public void create(SalesChannelRequest request) {
		ValidatedChannel input = validate(request);

		mapper.insert(
				input.name(),
				input.feeRate(),
				input.fixedFee(),
				input.note()
				);
	}

	public void update(
			Long id,
			SalesChannelRequest request
			) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException(
					"チャネルIDが不正です"
					);
		}

		if (mapper.selectById(id) == null) {
			throw new IllegalArgumentException(
					"対象のチャネルが存在しません"
					);
		}

		ValidatedChannel input = validate(request);

		int updated = mapper.update(
				id,
				input.name(),
				input.feeRate(),
				input.fixedFee(),
				input.note()
				);

		if (updated == 0) {
			throw new IllegalArgumentException(
					"チャネルの更新に失敗しました"
					);
		}
	}

	public void delete(Long id) {
		mapper.delete(id);
	}

	private ValidatedChannel validate(
			SalesChannelRequest request
			) {
		if (request == null) {
			throw new IllegalArgumentException(
					"リクエストが不正です"
					);
		}

		String name =
				request.name() == null
						? ""
						: request.name().trim();

		if (name.isEmpty()) {
			throw new IllegalArgumentException(
					"チャネル名は必須です"
					);
		}

		if (name.length() > 100) {
			throw new IllegalArgumentException(
					"チャネル名は100文字以内で入力してください"
					);
		}

		BigDecimal feeRate =
				request.feeRate() == null
						? BigDecimal.ZERO
						: request.feeRate();

		if (feeRate.compareTo(BigDecimal.ZERO) < 0
				|| feeRate.compareTo(MAX_FEE_RATE) > 0) {
			throw new IllegalArgumentException(
					"手数料率は0以上100以下で入力してください"
					);
		}

		if (decimalPlaces(feeRate) > 4) {
			throw new IllegalArgumentException(
					"手数料率は小数点以下4桁以内で入力してください"
					);
		}

		BigDecimal fixedFee =
				request.fixedFee() == null
						? BigDecimal.ZERO
						: request.fixedFee();

		if (fixedFee.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(
					"固定費は0以上で入力してください"
					);
		}

		if (fixedFee.compareTo(MAX_FIXED_FEE) > 0) {
			throw new IllegalArgumentException(
					"固定費の金額が大きすぎます"
					);
		}

		if (decimalPlaces(fixedFee) > 2) {
			throw new IllegalArgumentException(
					"固定費は小数点以下2桁以内で入力してください"
					);
		}

		String note =
				request.note() == null
						|| request.note().isBlank()
						? null
						: request.note().trim();

		return new ValidatedChannel(
				name,
				feeRate,
				fixedFee,
				note
				);
	}

	private int decimalPlaces(BigDecimal value) {
		return Math.max(
				0,
				value.stripTrailingZeros().scale()
				);
	}

	private record ValidatedChannel(
			String name,
			BigDecimal feeRate,
			BigDecimal fixedFee,
			String note
			) {
	}
}