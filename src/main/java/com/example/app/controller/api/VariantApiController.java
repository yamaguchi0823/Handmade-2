package com.example.app.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StockAdjustRequest;
import com.example.app.dto.StockDeltaRequest;
import com.example.app.dto.VariantCreateRequest;
import com.example.app.dto.VariantListRowDto;
import com.example.app.service.VariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class VariantApiController {

  private final VariantService variantService;

  @GetMapping
  public List<VariantListRowDto> search(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) Long materialId,
      @RequestParam(required = false) String status,
      @RequestParam(required = false, defaultValue = "ALL") String stockMode
  ) {
    return variantService.search(q, categoryId, materialId, status, stockMode);
  }
  
  // ★ delta:成功したら確定stockを返す（Reactが扱いやすい）
  @PostMapping("/{variantId}/stock-movements/delta")
  public Map<String, Integer> addDelta(
  	@PathVariable Long variantId, 
  	@RequestBody StockDeltaRequest req
  	) {
  	int stock =
  			variantService.addStockDelta(variantId, req.delta(), req.note());
  	return Map.of("stock", stock);
  }
  
  // adjustも同様に stock を返すと使いやすい（今はvoidでも動くけど、基本形として揃える）
  @PostMapping("/{variantId}/stock-movements/adjust")
  public Map<String, Integer> adjust(
  	@PathVariable Long variantId,
  	@RequestBody StockAdjustRequest req
  	) {
  	int stock = 
  			variantService.adjustStock(variantId, req.newStock(), req.note()); // ※後述：戻り値にする
  	return Map.of("stock",stock);
  }
  
  @PostMapping
  public void create(@RequestBody VariantCreateRequest req) {
  	variantService.createVariant(req);
  }
  
}
