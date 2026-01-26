package com.example.app.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StockDeltaRequest;
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
  
  @PostMapping("/{variantId}/stock-movements/delta")
  public void addDelta(
  	@PathVariable Long variantId, 
  	@RequestBody StockDeltaRequest req) {
  	variantService.addStockDelta(variantId, req.delta(), req.note());
  }
  
}
