-- Script tổng hợp sửa lỗi ReturnIn
USE Storedogo2;

PRINT '=== BẮT ĐẦU SỬA LỖI RETURNIN ===';

-- 1. Kiểm tra tất cả constraints hiện tại
PRINT '=== BƯỚC 1: KIỂM TRA CONSTRAINT HIỆN TẠI ===';
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK';

-- 2. Xóa TẤT CẢ constraints cũ liên quan đến TransactionType
PRINT '=== BƯỚC 2: XÓA TẤT CẢ CONSTRAINT CŨ ===';

-- Xóa constraint cũ CK_Inventory_Trans_44FF419A
IF EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_NAME = 'InventoryTransactions' 
    AND CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A'
)
BEGIN
    ALTER TABLE InventoryTransactions 
    DROP CONSTRAINT CK_Inventory_Trans_44FF419A;
    PRINT '✅ Đã xóa: CK_Inventory_Trans_44FF419A';
END

-- Xóa constraint cũ khác nếu có
IF EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_NAME = 'InventoryTransactions' 
    AND CONSTRAINT_NAME = 'CK_InventoryTransactions_TransactionType'
)
BEGIN
    ALTER TABLE InventoryTransactions 
    DROP CONSTRAINT CK_InventoryTransactions_TransactionType;
    PRINT '✅ Đã xóa: CK_InventoryTransactions_TransactionType';
END

-- Xóa bất kỳ constraint nào khác liên quan đến TransactionType
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
    PRINT '✅ Đã xóa: ' + @constraint_name;
END

-- 3. Tạo constraint mới duy nhất
PRINT '=== BƯỚC 3: TẠO CONSTRAINT MỚI ===';
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn', 'SaleOut', 'Adjustment', 'ReturnIn'));

PRINT '✅ Đã tạo constraint mới với ReturnIn';

-- 4. Kiểm tra constraint cuối cùng
PRINT '=== BƯỚC 4: KIỂM TRA CONSTRAINT CUỐI CÙNG ===';
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
PRINT '=== BƯỚC 5: TEST INSERT RETURNIN ===';
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 1, 'TEST-COMPLETE', 'Test hoàn chỉnh');
    
    PRINT '✅ Insert ReturnIn thành công!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-COMPLETE';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Lỗi khi insert ReturnIn: ' + ERROR_MESSAGE();
END CATCH

PRINT '=== HOÀN THÀNH SỬA LỖI RETURNIN ===';
PRINT 'Bây giờ bạn có thể test chức năng yêu cầu đổi trả trong ứng dụng!';
