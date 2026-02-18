package com.example.app.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.SalesChannelDto;

@Mapper
public interface SalesChannelMapper {
	
	List<SalesChannelDto> selectAll();
	
	SalesChannelDto selectById(@Param("id") Long id);
	
	int insert(
			@Param("name") String name,
			@Param("feeRate") BigDecimal feeRate,
			@Param("fixedFee") BigDecimal fixedFee,
			@Param("note") String note
			);
	
	int update(
			@Param("id") Long id,
			@Param("name") String name,
			@Param("feeRate") BigDecimal feeRate,
			@Param("fixedFee") BigDecimal fixedFee,
			@Param("note") String note		
			);

	int delete(@Param("id") Long id);
}
