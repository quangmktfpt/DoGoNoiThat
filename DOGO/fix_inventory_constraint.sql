-- Script sửa constraint của bảng InventoryTransactions
USE Storedogo2;

-- Bước 1: Kiểm tra constraint hiện tại
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions';

-- Bước 2: Xóa constraint cũ
DECLARE @constraint_name NVARCHAR(200)
SELECT @constraint_name = CONSTRAINT_NAME 
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions' 
  AND CONSTRAINT_NAME LIKE '%TransactionType%'

IF @constraint_name IS NOT NULL
BEGIN
    DECLARE @sql NVARCHAR(500) = 'ALTER TABLE InventoryTransactions DROP CONSTRAINT [' + @constraint_name + ']'
    EXEC sp_executesql @sql
    PRINT 'Đã xóa constraint cũ: ' + @constraint_name
END
ELSE
BEGIN
    PRINT 'Không tìm thấy constraint cũ'
END

-- Bước 3: Thêm constraint mới bao gồm ReturnIn
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

PRINT 'Đã thêm constraint mới với ReturnIn'

-- Bước 4: Kiểm tra kết quả
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions';

PRINT 'Hoàn thành! Bây giờ có thể sử dụng ReturnIn trong giao dịch hoàn trả.';
