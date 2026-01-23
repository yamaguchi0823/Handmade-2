package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
