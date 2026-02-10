package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.StockMovementRowDto;

@Mapper
public interface StockMovementMapper {
	
  int insert(
      @Param("variantId") Long variantId,
      @Param("movementType") String movementType,
      @Param("delta") int delta,
      @Param("qtyBefore") int qtyBefore,
      @Param("qtyAfter") int qtyAfter,
      @Param("refType") String refType,
      @Param("refId") Long refId,
      @Param("note") String note
  );
  
  List<StockMovementRowDto> selectByVariantId(
  		@Param("variantId") Long variantId,
  		@Param("limit") int limit
  		);
}
