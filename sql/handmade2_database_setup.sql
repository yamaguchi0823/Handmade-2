-- Handmade-2 local database setup
-- MySQL 8.x
--
-- This script is safe to run when the `handmade2` database does not exist yet.
-- It does not drop an existing database or table.

CREATE DATABASE IF NOT EXISTS handmade2
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE handmade2;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_categories_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS materials (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_materials_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_items_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS item_variants (
  id BIGINT NOT NULL AUTO_INCREMENT,
  item_id BIGINT NOT NULL,
  sku_code VARCHAR(100) NOT NULL,
  image_filename VARCHAR(255) NULL,
  color VARCHAR(100) NULL,
  size VARCHAR(100) NULL,
  category_id BIGINT NULL,
  material_id BIGINT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  stock INT NOT NULL DEFAULT 0,
  stock_alert_threshold INT NOT NULL DEFAULT 0,
  cost_price DECIMAL(12, 2) NOT NULL DEFAULT 0,
  price DECIMAL(12, 2) NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_item_variants_sku_code (sku_code),
  KEY idx_item_variants_item (item_id),
  KEY idx_item_variants_status (status),
  KEY idx_item_variants_category (category_id),
  KEY idx_item_variants_material (material_id),
  CONSTRAINT chk_item_variants_stock
    CHECK (stock >= 0),
  CONSTRAINT chk_item_variants_threshold
    CHECK (stock_alert_threshold >= 0),
  CONSTRAINT chk_item_variants_cost_price
    CHECK (cost_price >= 0),
  CONSTRAINT chk_item_variants_price
    CHECK (price >= 0),
  CONSTRAINT fk_item_variants_item
    FOREIGN KEY (item_id) REFERENCES items (id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_item_variants_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_item_variants_material
    FOREIGN KEY (material_id) REFERENCES materials (id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS sales_channels (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  fee_rate DECIMAL(7, 4) NOT NULL DEFAULT 0,
  fixed_fee DECIMAL(12, 2) NOT NULL DEFAULT 0,
  note TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_sales_channels_name (name),
  CONSTRAINT chk_sales_channels_fee_rate
    CHECK (fee_rate >= 0),
  CONSTRAINT chk_sales_channels_fixed_fee
    CHECK (fixed_fee >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS sales (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sold_at DATETIME NOT NULL,
  channel_id BIGINT NULL,
  note TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_sales_sold_at (sold_at),
  KEY idx_sales_channel (channel_id),
  CONSTRAINT fk_sales_channel
    FOREIGN KEY (channel_id) REFERENCES sales_channels (id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS sale_lines (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sale_id BIGINT NOT NULL,
  variant_id BIGINT NOT NULL,
  qty INT NOT NULL,
  unit_price DECIMAL(12, 2) NOT NULL,
  cost_price DECIMAL(12, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_sale_lines_sale (sale_id),
  KEY idx_sale_lines_variant (variant_id),
  CONSTRAINT chk_sale_lines_qty
    CHECK (qty <> 0),
  CONSTRAINT chk_sale_lines_unit_price
    CHECK (unit_price >= 0),
  CONSTRAINT chk_sale_lines_cost_price
    CHECK (cost_price >= 0),
  CONSTRAINT fk_sale_lines_sale
    FOREIGN KEY (sale_id) REFERENCES sales (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_sale_lines_variant
    FOREIGN KEY (variant_id) REFERENCES item_variants (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS stock_movements (
  id BIGINT NOT NULL AUTO_INCREMENT,
  variant_id BIGINT NOT NULL,
  movement_type VARCHAR(20) NOT NULL,
  delta INT NOT NULL,
  qty_before INT NOT NULL,
  qty_after INT NOT NULL,
  ref_type VARCHAR(20) NOT NULL,
  ref_id BIGINT NULL,
  note TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_stock_movements_variant_created (variant_id, created_at),
  KEY idx_stock_movements_reference (ref_type, ref_id),
  CONSTRAINT chk_stock_movements_qty_before
    CHECK (qty_before >= 0),
  CONSTRAINT chk_stock_movements_qty_after
    CHECK (qty_after >= 0),
  CONSTRAINT fk_stock_movements_variant
    FOREIGN KEY (variant_id) REFERENCES item_variants (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Initial channel names used by the sales screen.
-- Fees are intentionally zero; edit them later to match the actual service.
INSERT IGNORE INTO sales_channels (name, fee_rate, fixed_fee, note)
VALUES
  ('minne', 0, 0, '手数料は実際の条件に合わせて設定してください'),
  ('Creema', 0, 0, '手数料は実際の条件に合わせて設定してください'),
  ('BASE', 0, 0, '手数料は実際の条件に合わせて設定してください'),
  ('委託販売', 0, 0, '手数料は実際の条件に合わせて設定してください'),
  ('個別販売', 0, 0, '手数料は実際の条件に合わせて設定してください');

-- Verification
SHOW TABLES;
SELECT id, name, fee_rate, fixed_fee
FROM sales_channels
ORDER BY id;
