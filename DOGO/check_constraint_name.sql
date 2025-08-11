-- Script kiểm tra tên constraint của bảng InventoryTransactions
USE Storedogo2;

-- Kiểm tra tất cả constraints của bảng InventoryTransactions
SELECT 
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
ORDER BY tc.CONSTRAINT_TYPE, tc.CONSTRAINT_NAME;

-- Hoặc chỉ kiểm tra CHECK constraints
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions';

-- Hoặc kiểm tra constraint liên quan đến TransactionType
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions' 
  AND CHECK_CLAUSE LIKE '%TransactionType%';

PRINT 'Đã hiển thị tất cả constraints của bảng InventoryTransactions';
