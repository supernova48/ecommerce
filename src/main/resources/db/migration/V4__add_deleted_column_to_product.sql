ALTER TABLE product ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
-- Create an index to keep your filtered active product searches fast
CREATE INDEX idx_product_deleted ON product(deleted) WHERE deleted = FALSE;