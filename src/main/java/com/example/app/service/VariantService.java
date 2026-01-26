package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.VariantListRowDto;
import com.example.app.mapper.StockMovementMapper;
import com.example.app.mapper.VariantMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VariantService {
	
	private final VariantMapper variantMapper;
	private final StockMovementMapper stockMovementMapper;
	
	// 検索条件（キーワード、カテゴリ、素材、状態、在庫モード）
	public List<VariantListRowDto> search(
			String q,
			Long categoryId,
			Long materialId,
			String status,
			String stockMode
			){
		return variantMapper.search(
				q, categoryId, materialId, status, stockMode
				);
	}
	
	@Transactional
	public void addStockDelta(Long variantId, int delta, String note) {
		Integer beforeObj = variantMapper.selectStockForUpdate(variantId);
		if(beforeObj == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
		}
		
		int before = beforeObj;
		int after = before + delta;
		
		if(after < 0) {
			throw new IllegalArgumentException("在庫がマイナスになります");
		}
		
		variantMapper.updateStock(variantId, after);
		
		// movementTypeは簡単に：増ならIN,減ならOUT
		String movementType = (delta >= 0) ? "IN":"OUT";
		
		stockMovementMapper.insert(
				variantId, 
				movementType, 
				delta, 
				before, 
				after, 
				"MANUAL",
				null,
//				movementType, 
//				variantId, 
				note
				);
	}
	
	@Transactional
	public void adjustStock(Long variantId, int newStock, String note) {
		if(newStock < 0) {
			throw new IllegalArgumentException("在庫は0以上で入力してください！");
		}
		Integer beforeObj = variantMapper.selectStockForUpdate(variantId);
		if(beforeObj == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
		}
		
		int before = beforeObj;
		int after = newStock;
		int delta = after - before;
		
		// 変化がないなら何もしない（履歴を残したいなら削ってOK）
		if(delta == 0) {
			return;
		}
		
		int updated = variantMapper.updateStock(variantId, newStock);
		if(updated == 0) {
			throw new IllegalArgumentException("在庫更新に失敗しました");
		}
		
		stockMovementMapper.insert(
				variantId, 
				"ADJUST", 
				delta, 
				before, 
				after, 
				"MANUAL", 
				null, 
				note);
	}
	
}
