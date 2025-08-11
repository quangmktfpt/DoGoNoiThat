-- Script test insert ReturnIn
USE Storedogo2;

-- 1. Kiểm tra constraint hiện tại
SELECT 
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_TYPE = 'CHECK';

-- 2. Kiểm tra cấu trúc bảng
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'InventoryTransactions'
ORDER BY ORDINAL_POSITION;

-- 3. Test insert với UserID
BEGIN TRY
    INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes, UserID)
    VALUES ('TEST001', 'ReturnIn', 5, 'TEST-003', 'Test ReturnIn with UserID', 1);
    
    PRINT '✅ Insert ReturnIn với UserID thành công!';
    
    -- Xóa record test
    DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-003';
    PRINT '✅ Đã xóa record test';
    
END TRY
BEGIN CATCH
    PRINT '❌ Lỗi khi insert ReturnIn với UserID: ' + ERROR_MESSAGE();
    
    -- Thử insert không có UserID
    BEGIN TRY
        INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes)
        VALUES ('TEST001', 'ReturnIn', 5, 'TEST-004', 'Test ReturnIn without UserID');
        
        PRINT '✅ Insert ReturnIn không có UserID thành công!';
        
        -- Xóa record test
        DELETE FROM InventoryTransactions WHERE ReferenceID = 'TEST-004';
        PRINT '✅ Đã xóa record test';
        
    END TRY
    BEGIN CATCH
        PRINT '❌ Lỗi khi insert ReturnIn không có UserID: ' + ERROR_MESSAGE();
    END CATCH
END CATCH

-- 4. Kiểm tra dữ liệu hiện tại
SELECT TOP 5 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    ReferenceID,
    Notes,
    UserID,
    TransactionDate
FROM InventoryTransactions 
ORDER BY TransactionDate DESC;
