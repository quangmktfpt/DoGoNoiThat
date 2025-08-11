-- Script sửa CHECK constraint để cho phép ReturnIn
USE Storedogo2;

-- 1. Kiểm tra constraint hiện tại
PRINT '=== KIỂM TRA CONSTRAINT HIỆN TẠI ===';
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK';

-- 2. Xóa constraint cũ
PRINT '=== XÓA CONSTRAINT CŨ ===';
DECLARE @constraint_name NVARCHAR(128);

SELECT @constraint_name = tc.CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK'
  AND tc.CONSTRAINT_NAME LIKE '%TransactionType%';

IF @constraint_name IS NOT NULL
BEGIN
    DECLARE @sql NVARCHAR(500) = 'ALTER TABLE InventoryTransactions DROP CONSTRAINT [' + @constraint_name + ']';
    EXEC sp_executesql @sql;
    PRINT 'Đã xóa constraint: ' + @constraint_name;
END
ELSE
BEGIN
    PRINT 'Không tìm thấy constraint cần xóa';
END

-- 3. Tạo constraint mới với ReturnIn
PRINT '=== TẠO CONSTRAINT MỚI ===';
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn', 'SaleOut', 'Adjustment', 'ReturnIn'));

PRINT 'Đã tạo constraint mới với ReturnIn';

-- 4. Kiểm tra constraint mới
PRINT '=== KIỂM TRA CONSTRAINT MỚI ===';
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK';

-- 5. Test insert ReturnIn
PRINT '=== TEST INSERT RETURNIN ===';
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 5, 'TEST-RETURNIN', 'Test ReturnIn transaction');
    
    PRINT '✅ Insert ReturnIn thành công!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-RETURNIN';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Lỗi khi insert ReturnIn: ' + ERROR_MESSAGE();
END CATCH

PRINT '=== HOÀN THÀNH SỬA CONSTRAINT ===';
