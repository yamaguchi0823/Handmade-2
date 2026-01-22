package com.example.app.dto;
// DTO（返すJSONの型）を作る
import java.math.BigDecimal;

public record VariantListRowDto(
    Long id,// バリエID
    String skuCode,// SKU
    String itemName,// 作品名
    String color,// 色orモチーフ
    String size,// サイズ
    String status,// 在庫状況
    int stock,// 在庫数
    int stockAlertThreshold, //在庫しきい値
    BigDecimal price, // 価格
    BigDecimal costPrice, // コスト
    Long categoryId, // カテゴリID
    String categoryName, // カテゴリ名
    Long materialId, // 素材ID
    String materialName, // 素材名
    String imageUrl // 画像URL
) {}
