-- SKUを任意入力にするため、Nullを許可する
-- UNIQUE制約は維持し、未入力時は空文字ではなくNULLを保存する

ALTER TABLE item_variants
MODIFY COLUMN sku_code VARCHAR(100) NULL;