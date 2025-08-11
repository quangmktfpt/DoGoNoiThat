-- Script tự động sửa database - không cần chỉnh sửa gì
USE Storedogo2;

-- Bước 1: Tìm và xóa constraint cũ tự động
DECLARE @constraint_name NVARCHAR(200)
DECLARE @sql NVARCHAR(500)

SELECT @constraint_name = CONSTRAINT_NAME 
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE CONSTRAINT_SCHEMA = 'dbo' 
  AND TABLE_NAME = 'InventoryTransactions' 
  AND CONSTRAINT_NAME LIKE '%TransactionType%'

IF @constraint_name IS NOT NULL
BEGIN
    SET @sql = 'ALTER TABLE InventoryTransactions DROP CONSTRAINT [' + @constraint_name + ']'
    EXEC sp_executesql @sql
    PRINT 'Đã xóa constraint cũ: ' + @constraint_name
END
ELSE
BEGIN
    PRINT 'Không tìm thấy constraint cũ'
END

-- Bước 2: Thêm constraint mới
BEGIN TRY
    ALTER TABLE InventoryTransactions 
    ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
    CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'))
    PRINT 'Đã thêm constraint mới với ReturnIn'
END TRY
BEGIN CATCH
    PRINT 'Lỗi khi thêm constraint: ' + ERROR_MESSAGE()
END CATCH

-- Bước 3: Cập nhật dữ liệu cũ
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment'

PRINT 'Đã cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch hoàn trả'

-- Bước 4: Đảm bảo số lượng dương
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'ReturnIn'
  AND QuantityChange < 0

PRINT 'Đã sửa ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch có số lượng âm'

-- Bước 5: Kiểm tra kết quả
SELECT '=== KẾT QUẢ SAU KHI SỬA ===' as ThongBao
SELECT 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    LEFT(Notes, 50) as Notes
FROM InventoryTransactions 
WHERE Notes LIKE '%Hoàn trả sản phẩm%'
ORDER BY TransactionDate DESC

PRINT 'Hoàn thành! Hãy kiểm tra kết quả ở trên.'
