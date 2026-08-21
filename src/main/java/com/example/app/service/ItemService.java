package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.ItemCreateRequest;
import com.example.app.dto.ItemDto;
import com.example.app.dto.ItemUpdateRequest;
import com.example.app.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemMapper itemMapper;
	
	public List<ItemDto> listActive(){
		return itemMapper.selectActiveItems();
	}
	
	@Transactional
	public void create(ItemCreateRequest req) {
		if(req.name() == null || req.name().isBlank()){
			throw new IllegalArgumentException("作品名は必須です");
		}
		itemMapper.insert(req.name().trim(), req.description());
	}
	
	@Transactional
	public void update(long id, ItemUpdateRequest req) {
		if(req.name() == null || req.name().isBlank()) {
			throw new IllegalArgumentException("作品名は必須です");
		}
		int updated = itemMapper.update(id, req.name().trim(), req.description());
		if(updated == 0) throw new IllegalArgumentException("対象の作品が存在しません");
	}
	
	@Transactional
	public void deactivate(long id) {
		int activeVariantCount = itemMapper.countActiveVariantsByItemId(id);
		
		if (activeVariantCount > 0) {
			throw new IllegalArgumentException(
					"販売中のバリエーションが登録されている作品は無効化できません"
					);
		}
		int updated = itemMapper.deactivate(id);
		if(updated == 0) {
			throw new IllegalArgumentException("対象の作品が存在しません");
		}
		}
	
}
