-- Script chỉ cập nhật dữ liệu - không thay đổi constraint
USE Storedogo2;

-- Bước 1: Cập nhật dữ liệu cũ từ Adjustment thành PurchaseIn
UPDATE InventoryTransactions 
SET TransactionType = 'PurchaseIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

PRINT 'Đã cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch hoàn trả';

-- Bước 2: Đảm bảo số lượng dương
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'PurchaseIn'
  AND QuantityChange < 0;

PRINT 'Đã sửa ' + CAST(@@ROWCOUNT AS VARCHAR) + ' giao dịch có số lượng âm';

-- Bước 3: Kiểm tra kết quả
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

PRINT 'Hoàn thành! Các giao dịch hoàn trả giờ sẽ hiển thị là "Nhập kho" với số lượng dương.';
