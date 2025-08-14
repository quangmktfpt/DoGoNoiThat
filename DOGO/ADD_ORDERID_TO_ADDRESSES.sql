-- =====================================================
-- THÊM CỘT ORDERID VÀO BẢNG ADDRESSES
-- =====================================================

-- 1. Thêm cột OrderID vào bảng Addresses
ALTER TABLE Addresses 
ADD OrderID INT NULL;

-- 2. Thêm khóa ngoại để liên kết với bảng Orders
ALTER TABLE Addresses 
ADD CONSTRAINT FK_Addresses_Orders 
FOREIGN KEY (OrderID) REFERENCES Orders(OrderID);

-- 3. Tạo index để tối ưu hiệu suất truy vấn
CREATE INDEX IX_Addresses_OrderID ON Addresses(OrderID);

-- 4. Cập nhật dữ liệu hiện có (nếu cần)
-- Lưu ý: Chỉ chạy nếu muốn cập nhật dữ liệu cũ
-- UPDATE Addresses 
-- SET OrderID = (
--     SELECT TOP 1 o.OrderID 
--     FROM Orders o 
--     WHERE o.DeliveryAddressID = Addresses.AddressID
-- )
-- WHERE OrderID IS NULL;

-- 5. Kiểm tra kết quả
SELECT 
    'Addresses table structure:' as Info,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses'
ORDER BY ORDINAL_POSITION;

-- 6. Kiểm tra khóa ngoại
SELECT 
    'Foreign Keys:' as Info,
    fk.name as FK_Name,
    OBJECT_NAME(fk.parent_object_id) as Table_Name,
    COL_NAME(fkc.parent_object_id, fkc.parent_column_id) as Column_Name,
    OBJECT_NAME(fk.referenced_object_id) as Referenced_Table,
    COL_NAME(fkc.referenced_object_id, fkc.referenced_column_id) as Referenced_Column
FROM sys.foreign_keys fk
INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
WHERE OBJECT_NAME(fk.parent_object_id) = 'Addresses';

-- =====================================================
-- LỆNH ROLLBACK (NẾU CẦN HOÀN TÁC)
-- =====================================================
/*
-- Xóa khóa ngoại
ALTER TABLE Addresses 
DROP CONSTRAINT FK_Addresses_Orders;

-- Xóa index
DROP INDEX IX_Addresses_OrderID ON Addresses;

-- Xóa cột OrderID
ALTER TABLE Addresses 
DROP COLUMN OrderID;
*/
