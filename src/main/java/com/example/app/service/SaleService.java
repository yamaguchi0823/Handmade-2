package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.ChannelProfitRowDto;
import com.example.app.dto.SaleCreateParam;
import com.example.app.dto.SaleCreateRequest;
import com.example.app.dto.SaleDetailDto;
import com.example.app.dto.SaleHeaderDto;
import com.example.app.dto.SaleLineCreateRequest;
import com.example.app.dto.SaleLineRowDto;
import com.example.app.dto.SaleListRowDto;
import com.example.app.exception.StockConflictException;
import com.example.app.mapper.SaleMapper;
import com.example.app.mapper.StockMovementMapper;
import com.example.app.mapper.VariantMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {
	
	private final SaleMapper saleMapper;
	private final VariantMapper variantMapper;
	private final StockMovementMapper stockMovementMapper;
	
	@Transactional
	public Long createSale(SaleCreateRequest req) {
		// 1) バリデーション（最低限）
		if (req == null) {
			throw new IllegalArgumentException("リクエストが不正です");
		}
		List<SaleLineCreateRequest> lines = req.lines();
		if (lines == null || lines.isEmpty()) {
			throw new IllegalArgumentException("明細がありません");
		}
		
		LocalDateTime soldAt =
				(req.soldAt() != null) ? req.soldAt() : LocalDateTime.now();
		
		// 2) sales（ヘッダ）作成
		SaleCreateParam p = new SaleCreateParam();
		p.setSoldAt(soldAt);
		p.setChannelId(req.channelId());
		p.setNote(req.note());
		
		saleMapper.insertSale(p);
		Long saleId = p.getId(); // ※採番されたID
		
		// 3) 明細ごとに：価格補完→sale_lines →在庫更新→stock_movements
		for (SaleLineCreateRequest line : lines) {
			if(line.variantId() == null) {
				throw new IllegalArgumentException("variantIdは必須です");
			}
			int qty = (line.qty() == null) ? 0 : line.qty();
			if(qty == 0) {
				throw new IllegalArgumentException("qtyは0以外で入力してください");
			}
		
			// 価格補完（nullならDBから）
			BigDecimal unitPrice = line.unitPrice();
			BigDecimal costPrice = line.costPrice();
			
			if ( unitPrice == null || costPrice == null ) {
				Map<String, BigDecimal> prices = saleMapper.selectVariantPrices(line.variantId());
				if(prices == null || prices.isEmpty()) {
					throw new IllegalArgumentException("対象のバリエーションが存在しません:id=" + line.variantId());	
				}
				if (unitPrice == null) unitPrice = nz(prices.get("unitPrice"));
        if (costPrice == null) costPrice = nz(prices.get("costPrice"));
			}
			
			if (unitPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("unitPriceは0以上");
      if (costPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("costPriceは0以上");

      // sale_lines insert（販売時点の値を保存）
      saleMapper.insertSaleLine(saleId, line.variantId(), qty, unitPrice, costPrice);

      // 在庫更新（販売 qty>0 => delta=-qty で減る、返品 qty<0 => delta=-qty で増える）
      Integer beforeObj = variantMapper.selectStock(line.variantId());
      if (beforeObj == null) throw new IllegalArgumentException("対象のバリエーションが存在しません：id=" + line.variantId());
      int before = beforeObj;

      int deltaStock = -qty;
      int updated = variantMapper.updateStockByDelta(line.variantId(), deltaStock);
      if (updated == 0) {
        // 在庫不足など（あなたの既存設計に合わせる）
        throw new StockConflictException("在庫が不足しています（variantId=" + line.variantId() + "）");
      }

      int after = variantMapper.selectStock(line.variantId());

      // movementType は在庫増減ベースで決める
      String movementType = (deltaStock >= 0) ? "IN" : "OUT";

      stockMovementMapper.insert(
          line.variantId(),
          movementType,
          deltaStock,     // ★在庫の増減量として記録（-2 / +1）
          before,
          after,
          "SALE",
          saleId,
          (req.note() == null || req.note().isBlank()) ? null : req.note().trim()
      );
    }
    return saleId;
  }
  private static BigDecimal nz(BigDecimal v) {
    return (v == null) ? BigDecimal.ZERO : v;
	}
  
  public List<SaleListRowDto> listRecent(int limit){
  	int lim = (limit <= 0 || limit > 200 ) ? 50 : limit;
  	return saleMapper.selectRecentSales(lim);
  }
  
  public SaleDetailDto getDetail(long saleId) {
  	SaleHeaderDto header = saleMapper.selectSaleHeader(saleId);
  	if (header == null ) {
  		throw new IllegalArgumentException("販売が存在しません：id=" + saleId);
  	}
  	List<SaleLineRowDto> lines = saleMapper.selectSaleLines(saleId);
  	return new SaleDetailDto(
  			header.id(),
  			header.soldAt(),
  			header.channelId(),
  			header.note(),
  			lines);
  }
  
  public List<ChannelProfitRowDto> channelProfit(LocalDate from, LocalDate to){
  	return saleMapper.selectChannelProfit(from, to);
  }
	
}
