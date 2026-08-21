package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.dto.ItemDto;

@Mapper
public interface ItemMapper {
	List<ItemDto> selectActiveItems();
	
	ItemDto selectById(@Param("id") long id);
	
	int insert(
			@Param("name") String name, 
			@Param("description") String description
			);
	
	int update(
			@Param("id") long id,
			@Param("name") String name,
			@Param("description") String description
			);
	
	int countActiveVariantsByItemId(@Param("itemId") long itemId);
	
	int deactivate(@Param("id") long id);
}
