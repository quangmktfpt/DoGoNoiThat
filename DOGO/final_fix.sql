-- Script cuối cùng - đơn giản và không lỗi
USE Storedogo2;

-- Bước 1: Thêm constraint mới (bỏ qua việc xóa constraint cũ)
ALTER TABLE InventoryTransactions 
ADD CONSTRAINT CK_InventoryTransactions_TransactionType 
CHECK (TransactionType IN ('PurchaseIn','SaleOut','Adjustment','ReturnIn'));

PRINT 'Đã thêm constraint mới với ReturnIn';

-- Bước 2: Cập nhật dữ liệu cũ
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

PRINT 'Đã cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch hoàn trả';

-- Bước 3: Đảm bảo số lượng dương
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'ReturnIn'
  AND QuantityChange < 0;

PRINT 'Đã sửa ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch có số lượng âm';

-- Bước 4: Kiểm tra kết quả
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

PRINT 'Hoàn thành!';
