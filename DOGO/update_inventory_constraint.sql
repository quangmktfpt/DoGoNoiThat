-- Script cập nhật constraint cho bảng InventoryTransactions
USE Storedogo2;

-- Bước 1: Xóa constraint cũ
ALTER TABLE InventoryTransactions 
DROP CONSTRAINT CK_Inventory_Trans_44FF419A;

-- Bước 2: Thêm constraint mới cho phép ReturnIn
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_Inventory_Trans_44FF419A 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

-- Bước 3: Kiểm tra lại constraint mới
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions'
  AND CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A';

PRINT 'Đã cập nhật constraint thành công!';
PRINT 'Bây giờ có thể sử dụng ReturnIn trong TransactionType';
