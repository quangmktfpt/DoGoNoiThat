-- Script đơn giản để sửa database - phiên bản an toàn
USE Storedogo2;

-- Bước 1: Kiểm tra constraint hiện tại
SELECT CONSTRAINT_NAME, CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE TABLE_NAME = 'InventoryTransactions';

-- Bước 2: Xóa constraint cũ (nếu có)
-- Thay thế [TÊN_CONSTRAINT] bằng tên thực tế từ bước 1
-- Ví dụ: ALTER TABLE InventoryTransactions DROP CONSTRAINT CK__Inventory__Transa__12345678;

-- Bước 3: Thêm constraint mới
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

PRINT 'Đã thêm constraint mới với ReturnIn';

-- Bước 4: Cập nhật dữ liệu cũ
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

PRINT 'Đã cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch hoàn trả';

-- Bước 5: Đảm bảo số lượng dương
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'ReturnIn'
  AND QuantityChange < 0;

PRINT 'Đã sửa ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch có số lượng âm';

-- Bước 6: Kiểm tra kết quả
SELECT '=== KẾT QUẢ SAU KHI SỬA ===' as ThongBao;
SELECT 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    LEFT(Notes, 50) as Notes
FROM InventoryTransactions 
WHERE Notes LIKE '%Hoàn trả sản phẩm%'
ORDER BY TransactionDate DESC;
