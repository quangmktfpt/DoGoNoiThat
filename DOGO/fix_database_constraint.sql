-- Script sửa database để hỗ trợ loại giao dịch ReturnIn
USE Storedogo2;
GO

-- Bước 1: Xóa constraint cũ
ALTER TABLE InventoryTransactions 
DROP CONSTRAINT CK__Inventory__Transa__[TÊN_CONSTRAINT];

-- Hoặc nếu không biết tên constraint, dùng cách này:
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
END

-- Bước 2: Thêm constraint mới bao gồm ReturnIn
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

-- Bước 3: Cập nhật các giao dịch hoàn trả cũ từ Adjustment thành ReturnIn
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

-- Bước 4: Đảm bảo số lượng là dương cho các giao dịch hoàn trả
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'ReturnIn'
  AND QuantityChange < 0;

-- Bước 5: Kiểm tra kết quả
SELECT 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    Notes,
    TransactionDate
FROM InventoryTransactions 
WHERE Notes LIKE '%Hoàn trả sản phẩm%'
ORDER BY TransactionDate DESC;

-- Bước 6: Kiểm tra tổng quan các loại giao dịch
SELECT 
    TransactionType,
    COUNT(*) as SoLuong,
    SUM(CASE WHEN QuantityChange > 0 THEN 1 ELSE 0 END) as SoLuongDuong,
    SUM(CASE WHEN QuantityChange < 0 THEN 1 ELSE 0 END) as SoLuongAm
FROM InventoryTransactions 
GROUP BY TransactionType
ORDER BY TransactionType;

PRINT 'Đã cập nhật database thành công!';
PRINT 'Các giao dịch hoàn trả giờ sẽ hiển thị là "Hoàn trả" với số lượng dương.';
