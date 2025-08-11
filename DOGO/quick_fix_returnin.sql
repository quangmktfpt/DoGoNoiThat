-- Script sửa nhanh lỗi ReturnIn
USE Storedogo2;

-- Xóa constraint cũ (nếu có)
IF EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_NAME = 'InventoryTransactions' 
    AND CONSTRAINT_TYPE = 'CHECK'
    AND CONSTRAINT_NAME LIKE '%TransactionType%'
)
BEGIN
    DECLARE @constraint_name NVARCHAR(128);
    SELECT @constraint_name = CONSTRAINT_NAME 
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_NAME = 'InventoryTransactions' 
    AND CONSTRAINT_TYPE = 'CHECK'
    AND CONSTRAINT_NAME LIKE '%TransactionType%';
    
    DECLARE @sql NVARCHAR(500) = 'ALTER TABLE InventoryTransactions DROP CONSTRAINT [' + @constraint_name + ']';
    EXEC sp_executesql @sql;
    PRINT 'Đã xóa constraint cũ: ' + @constraint_name;
END

-- Tạo constraint mới với ReturnIn
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn', 'SaleOut', 'Adjustment', 'ReturnIn'));

PRINT 'Đã tạo constraint mới với ReturnIn';

-- Test
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 1, 'TEST-QUICK', 'Quick test');
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-QUICK';
    PRINT '✅ Test thành công - ReturnIn đã hoạt động!';
END TRY
BEGIN CATCH
    PRINT '❌ Test thất bại: ' + ERROR_MESSAGE();
END CATCH
