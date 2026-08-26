-- バリエーションを画面上で識別する名称を追加
-- 既存データを維持するためDBではNULLを許可する
-- 新規登録・編集時の必須チェックはアプリ側で行う

ALTER TABLE item_variants
ADD COLUMN variant_name VARCHAR(100) NULL
AFTER item_id;