-- Script debug vấn đề inventory transaction
USE Storedogo2;

-- 1. Kiểm tra tất cả constraints của bảng InventoryTransactions
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions';

-- 2. Kiểm tra cấu trúc bảng InventoryTransactions
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'InventoryTransactions'
ORDER BY ORDINAL_POSITION;

-- 3. Kiểm tra dữ liệu hiện tại trong InventoryTransactions
SELECT TOP 10 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    ReferenceID,
    Notes,
    TransactionDate
FROM InventoryTransactions 
ORDER BY TransactionDate DESC;

-- 4. Thử insert một record test với ReturnIn
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
    VALUES (1, 'ReturnIn', 5, 'TEST-001', 'Test ReturnIn transaction');
    
    PRINT '✅ Insert ReturnIn thành công!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-001';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Lỗi khi insert ReturnIn: ' + ERROR_MESSAGE();
    PRINT 'Error Number: ' + CAST(ERROR_NUMBER() AS VARCHAR);
    PRINT 'Error State: ' + CAST(ERROR_STATE() AS VARCHAR);
END CATCH
