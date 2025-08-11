-- Lệnh SQL đúng để kiểm tra CHECK constraints
USE Storedogo2;

SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions';
