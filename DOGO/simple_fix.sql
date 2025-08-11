-- Script đơn giản để sửa database
USE Storedogo2;

-- Tìm và xóa constraint cũ
DECLARE @constraint_name NVARCHAR(200)
SELECT @constraint_name = CONSTRAINT_NAME 
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE CONSTRAINT_SCHEMA = 'dbo' 
  AND TABLE_NAME = 'InventoryTransactions' 
  AND CONSTRAINT_NAME LIKE '%TransactionType%'

IF @constraint_name IS NOT NULL
BEGIN
    DECLARE @sql NVARCHAR(500) = 'ALTER TABLE InventoryTransactions DROP CONSTRAINT ' + @constraint_name
    EXEC sp_executesql @sql
    PRINT 'Đã xóa constraint cũ: ' + @constraint_name
END

-- Thêm constraint mới
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

PRINT 'Đã thêm constraint mới với ReturnIn'

-- Cập nhật dữ liệu cũ
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

PRINT 'Đã cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch hoàn trả'

-- Đảm bảo số lượng dương
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'ReturnIn'
  AND QuantityChange < 0;

PRINT 'Đã sửa ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch có số lượng âm'

-- Kiểm tra kết quả
SELECT 'Kết quả sau khi sửa:' as ThongBao
SELECT 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    LEFT(Notes, 50) as Notes
FROM InventoryTransactions 
WHERE Notes LIKE '%Hoàn trả sản phẩm%'
ORDER BY TransactionDate DESC;
