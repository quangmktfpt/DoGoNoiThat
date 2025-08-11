-- Lệnh SQL đúng để kiểm tra constraint của bảng InventoryTransactions
USE Storedogo2;

-- Cách 1: Join với TABLE_CONSTRAINTS để lọc theo TABLE_NAME
SELECT 
    tc.CONSTRAINT_NAME,
    cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc 
    ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions'
  AND tc.CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A';

-- Cách 2: Kiểm tra tất cả CHECK constraints (không lọc theo bảng)
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE CONSTRAINT_NAME = 'CK_Inventory_Trans_44FF419A';
