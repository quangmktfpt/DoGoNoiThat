-- Script xuất database Storedogo2
USE Storedogo2;

-- 1. Xuất cấu trúc bảng InventoryTransactions
PRINT '-- Cấu trúc bảng InventoryTransactions';
PRINT 'CREATE TABLE InventoryTransactions (';
PRINT '    TransactionID INT IDENTITY(1,1) PRIMARY KEY,';
PRINT '    ProductID VARCHAR(50) NOT NULL,';
PRINT '    TransactionDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),';
PRINT '    TransactionType VARCHAR(50) NOT NULL,';
PRINT '    QuantityChange INT NOT NULL,';
PRINT '    ReferenceID VARCHAR(100),';
PRINT '    Notes NVARCHAR(500),';
PRINT '    UserID INT';
PRINT ');';
PRINT '';

-- 2. Xuất constraints
PRINT '-- Constraints';
SELECT 
    'ALTER TABLE ' + TABLE_NAME + ' ADD CONSTRAINT ' + CONSTRAINT_NAME + 
    CASE 
        WHEN CONSTRAINT_TYPE = 'CHECK' THEN ' CHECK ' + CHECK_CLAUSE
        WHEN CONSTRAINT_TYPE = 'FOREIGN KEY' THEN ' FOREIGN KEY (' + COLUMN_NAME + ') REFERENCES ' + REFERENCED_TABLE_NAME + '(' + REFERENCED_COLUMN_NAME + ')'
        ELSE ''
    END + ';'
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu ON tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME
WHERE tc.TABLE_NAME = 'InventoryTransactions';

-- 3. Xuất dữ liệu mẫu
PRINT '';
PRINT '-- Dữ liệu mẫu InventoryTransactions';
SELECT TOP 10 
    'INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes, UserID) VALUES (''' + 
    ProductID + ''', ''' + TransactionType + ''', ' + CAST(QuantityChange AS VARCHAR) + ', ''' + 
    ISNULL(ReferenceID, '') + ''', ''' + ISNULL(Notes, '') + ''', ' + 
    ISNULL(CAST(UserID AS VARCHAR), 'NULL') + ');'
FROM InventoryTransactions 
ORDER BY TransactionDate DESC;
