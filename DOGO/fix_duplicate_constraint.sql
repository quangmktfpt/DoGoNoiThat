-- Script xóa constraint trùng lặp và tạo lại
USE Storedogo2;

-- 1. Kiểm tra chi tiết constraint hiện tại
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE,
    tc.TABLE_NAME
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A';

-- 2. Xóa constraint cũ hoàn toàn
ALTER TABLE InventoryTransactions 
DROP CONSTRAINT CK_Inventory_Trans_44FF419A;

-- 3. Tạo constraint mới với tên khác
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_Inventory_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

-- 4. Kiểm tra lại constraint mới
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
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 5, 'TEST-002', 'Test ReturnIn after fix');
    
    PRINT '✅ Insert ReturnIn thành công sau khi fix!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-002';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Vẫn lỗi khi insert ReturnIn: ' + ERROR_MESSAGE();
END CATCH

PRINT 'Hoàn thành fix constraint!';
