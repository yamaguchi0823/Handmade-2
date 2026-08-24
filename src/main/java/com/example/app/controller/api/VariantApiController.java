package com.example.app.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.dto.StockAdjustRequest;
import com.example.app.dto.StockDeltaRequest;
import com.example.app.dto.StockMovementRowDto;
import com.example.app.dto.VariantCreateRequest;
import com.example.app.dto.VariantListRowDto;
import com.example.app.dto.VariantUpdateRequest;
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
  public Map<String, Long> create(
		  @RequestBody VariantCreateRequest req
		  ) {
	  long variantId = variantService.createVariant(req);
	  return Map.of("variantId", variantId);
  }
  
//  @PostMapping
//  public void create(@RequestBody VariantCreateRequest req) {
//  	variantService.createVariant(req);
//  }
  
  @PutMapping("/{variantId}")
  public void update(
  		@PathVariable long variantId,
  		@RequestBody VariantUpdateRequest req
  		) {
  	variantService.updateVariant(variantId, req);
  }
  
  @GetMapping("/{variantId}/stock-movements")
  public List<StockMovementRowDto> history(
  		@PathVariable Long variantId,
  		@RequestParam(required=false, defaultValue="50") int limit
  		){
  	return variantService.getStockHistory(variantId, limit);
  }
  
  @PostMapping(value= "/{variantId}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> uploadImage(
  		@PathVariable long variantId,
  		@RequestPart("file")  MultipartFile file
  		) throws IOException {
  	String filename = variantService.uploadVariantImage(variantId, file);
				return Map.of("imageUrl", "/uploads/" +filename);
  }
  
  @DeleteMapping("/{variantId}/image")
  public void deleteImage(
  		@PathVariable long variantId
  		) {
  	variantService.deleteVariantImage(variantId);
  }
}
