package com.example.app.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.dto.StockMovementRowDto;
import com.example.app.dto.VariantCreateRequest;
import com.example.app.dto.VariantListRowDto;
import com.example.app.dto.VariantUpdateRequest;
import com.example.app.exception.StockConflictException;
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
			String stockMode) {
		return variantMapper.search(
				q, categoryId, materialId, status, stockMode);
	}

	@Transactional
	public int addStockDelta(Long variantId, int delta, String note) {
		
		// 1) 更新前在庫（履歴用）
		Integer beforeObj = variantMapper.selectStock(variantId);
	  if (beforeObj == null) {
	    throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
	  }

		int before = beforeObj;
		
		// 2) delta更新（マイナスになる更新はSQLが拒否 → updated=0）
		int updated = variantMapper.updateStockByDelta(variantId, delta);
		if(updated == 0) {
			// 在庫不足など
			throw new StockConflictException("在庫が不足しています");
		}
		
		// 3) 確定後在庫（フロントに返す＆履歴用）
		int after = variantMapper.selectStock(variantId);
		
		// 4) 履歴（movementTypeは増減でIN/OUT）
		String movementType = (delta >= 0) ? "IN" : "OUT";
		
		stockMovementMapper.insert(
				variantId, 
				movementType, 
				delta, 
				before, 
				after, 
				"MANUAL", 
				null, 
				note
				);
		return after;
	}
	

	@Transactional
	public int adjustStock(Long variantId, int newStock, String note) {
		if (newStock < 0) {
			throw new IllegalArgumentException("在庫は0以上で入力してください！");
		}
		Integer beforeObj = variantMapper.selectStockForUpdate(variantId);
		if (beforeObj == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
		}

		int before = beforeObj;
		int after = newStock;
		int delta = after - before;


		int updated = variantMapper.updateStock(variantId, newStock);
		if (updated == 0) {
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
		return after;
	}
	
	@Transactional
	public int applyDelta(long variantId, int delta) {
	  int updated = variantMapper.updateStockByDelta(variantId, delta);
	  if (updated == 0) {
	    // ここが「在庫不足」や「競合」を表す
	    throw new StockConflictException("在庫が不足しています");
	  }
	  return variantMapper.selectStock(variantId);
	}
	
	@Transactional
	public void createVariant(VariantCreateRequest req) {
		// バリデーション+デフォルト補完
		if(req.itemId() == null || req.itemId() <= 0) {
			throw new IllegalArgumentException("作品を選択してください");
		}
		if(req.skuCode() == null || req.skuCode().isBlank()) {
			throw new IllegalArgumentException("SKUは必須です");
		}

		int stock = (req.stock() == null) ? 0 : req.stock();
		if(stock < 0) {
			throw new IllegalArgumentException("在庫は0以上で入力してくれよな！");
		}
		
//		在庫警告閾値指定
		int threshold = (req.stockAlertThreshold() == null) ? 0 : req.stockAlertThreshold();
		if(threshold < 0) {
			throw new IllegalArgumentException("在庫しきい値は0以上で入力してくれよな！");
		}
		
		
		String status = (req.status() == null || req.status().isBlank())
				? "ACTIVE"
				: req.status().trim();
		
		BigDecimal price = (req.price() == null) ? BigDecimal.ZERO : req.price();
		if(price.compareTo(BigDecimal.ZERO)<0) {
			throw new IllegalArgumentException("価格は0以上で入力してください");
		}
		variantMapper.insertVariant(
				req.itemId(),
				req.skuCode().trim(),
				stock, 
				threshold,
				status, 
				price
				);
	}
	
	@Transactional
	public void updateVariant(long variantId, VariantUpdateRequest req) {
		// 1) 対象存在チェック（簡易：stockを受け取れるかで確認）
		Integer beforeObj = variantMapper.selectStockForUpdate(variantId);
		if(beforeObj == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません:id=" + variantId);
		}
		// 2) 入力補完&バリデーション
		String status = (req.status() == null || req.status().isBlank())
				? "ACTIVE"
				: req.status().trim();
		
		if(!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
			throw new IllegalArgumentException("statusはACTIVE/INACTIVEのみです");
		}
		
		int threshold = (req.stockAlertThreshold() == null) ? 0 : req.stockAlertThreshold();
		if(threshold < 0) {
			throw new IllegalArgumentException("在庫しきい値は0以上で入力してください");
		}
		
		BigDecimal price = (req.price() == null) ? BigDecimal.ZERO : req.price();
		if(price.compareTo(BigDecimal.ZERO)<0) {
			throw new IllegalArgumentException("価格は0以上で入力してください");
		}
		
		// 3) 更新
		int updated = variantMapper.updateVariantFields(variantId, status, threshold, price);
		if(updated == 0) {
			throw new IllegalArgumentException("更新に失敗しました:id=" + variantId);
		}
	}
	
	// 最新50件とかだけ返す（重くしない）
	public List<StockMovementRowDto> getStockHistory(Long variantId,int limit){
		int safeLimit = (limit <= 0 || limit > 200) ? 50 : limit;
		// 存在チェック（無くても動く）
		Integer s = variantMapper.selectStock(variantId);
		if(s == null) throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
		
		return stockMovementMapper.selectByVariantId(variantId, safeLimit);
	}
	
	
	@Transactional
	public String uploadVariantImage(long variantId, MultipartFile file) throws IOException {
		if(file == null || file.isEmpty()) {
			throw new IllegalArgumentException("画像ファイルを選択してください");
		}
		// 存在チェック（簡易）
		Integer stock = variantMapper.selectStock(variantId);
		if(stock == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + variantId);
		}
		
		String original = file.getOriginalFilename();
		String ext = "";
		if(original != null && original.contains(".")) {
			ext = original.substring(original.lastIndexOf(".")).toLowerCase();
		}
		if(!ext.matches("\\.(png|jpg|jpeg|webp|gif)")) {
			throw new IllegalArgumentException("png/jpg/jpeg/webp/gifのみ対応です");
		}
		
		Path dir = Paths.get("uploads");
		Files.createDirectories(dir);
		
		String filename = "v" + variantId + "_" + UUID.randomUUID() + ext;
		Path savePath = dir.resolve(filename);
		
		Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
		
		variantMapper.updateImageFilename(variantId, filename);
		return filename;
	}
	
	@Transactional
	public void deleteVariantImage(long variantId) {
		Integer stock = variantMapper.selectStock(variantId);
		if(stock == null) {
			throw new IllegalArgumentException("対象のバリエーションが存在しません:id=" + variantId);
		}
		
		int updated = variantMapper.clearImageFilename(variantId);
		if(updated == 0) {
			throw new IllegalArgumentException("画像削除に失敗しました:id=" + variantId);
		}
		// 今回は「ファイルの物理削除はしない」（安全＆簡単）
	}
}
