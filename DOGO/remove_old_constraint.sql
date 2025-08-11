-- Script xóa constraint cũ CK_Inventory_Trans_44FF419A
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

-- 2. Xóa constraint cũ CK_Inventory_Trans_44FF419A
PRINT '=== XÓA CONSTRAINT CŨ ===';
IF EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_NAME = 'InventoryTransactions' 
    AND CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A'
)
BEGIN
    ALTER TABLE InventoryTransactions 
    DROP CONSTRAINT CK_Inventory_Trans_44FF419A;
    PRINT '✅ Đã xóa constraint cũ: CK_Inventory_Trans_44FF419A';
END
ELSE
BEGIN
    PRINT '❌ Không tìm thấy constraint CK_Inventory_Trans_44FF419A';
END

-- 3. Kiểm tra lại sau khi xóa
PRINT '=== KIỂM TRA SAU KHI XÓA ===';
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK';

-- 4. Test insert ReturnIn
PRINT '=== TEST INSERT RETURNIN ===';
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 1, 'TEST-REMOVE-OLD', 'Test sau khi xóa constraint cũ');
    
    PRINT '✅ Insert ReturnIn thành công sau khi xóa constraint cũ!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-REMOVE-OLD';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Vẫn lỗi khi insert ReturnIn: ' + ERROR_MESSAGE();
END CATCH

PRINT '=== HOÀN THÀNH XÓA CONSTRAINT CŨ ===';
