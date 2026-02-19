package com.example.app.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.ChannelProfitRowDto;
import com.example.app.dto.SaleCreateParam;
import com.example.app.dto.SaleHeaderDto;
import com.example.app.dto.SaleLineRowDto;
import com.example.app.dto.SaleListRowDto;

@Mapper
public interface SaleMapper {
	
	int insertSale(
			SaleCreateParam param
//			@Param("soldAt") java.time.LocalDateTime soldAt,
//			@Param("channel") Long channelId,
//			@Param("note") String note
			);
	
	int insertSaleLine(
			@Param("saleId") Long saleId,
			@Param("variantId") Long variantId,
			@Param("qty") int qty,
			@Param("unitPrice") BigDecimal unitPrice,
			@Param("costPrice") BigDecimal costPrice
			);
	
	// umit_price / cost_price のデフォルト補完用
	Map<String, BigDecimal> selectVariantPrices(
			@Param("variantId") Long variantId
			);
	
	List<SaleListRowDto> selectRecentSales(
			@Param("limit") int limit
			);
	
	SaleHeaderDto selectSaleHeader(
			@Param("saleId") long saleId
			);
	
	List<SaleLineRowDto> selectSaleLines(
			@Param("saleId") long saleId
			);
	
	List<ChannelProfitRowDto> selectChannelProfit(
			@Param("from") LocalDate from,
			@Param("to") LocalDate to
			);
}
