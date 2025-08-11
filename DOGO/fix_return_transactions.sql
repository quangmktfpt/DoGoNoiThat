-- Script sửa các giao dịch hoàn trả sản phẩm cũ
-- Chuyển từ "Adjustment" (Xuất kho khác) thành "ReturnIn" (Hoàn trả)
-- Và đảm bảo số lượng là dương

-- Bước 1: Cập nhật các giao dịch có ghi chú "Hoàn trả sản phẩm" từ Adjustment thành ReturnIn
UPDATE InventoryTransactions 
SET TransactionType = 'ReturnIn'
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND TransactionType = 'Adjustment';

-- Bước 2: Đảm bảo số lượng là dương cho các giao dịch hoàn trả
UPDATE InventoryTransactions 
SET QuantityChange = ABS(QuantityChange)
WHERE Notes LIKE '%Hoàn trả sản phẩm%' 
  AND (TransactionType = 'ReturnIn' OR TransactionType = 'PurchaseIn')
  AND QuantityChange < 0;

-- Bước 3: Hiển thị kết quả để kiểm tra
SELECT 
    TransactionID,
    ProductID,
    TransactionType,
    QuantityChange,
    Notes,
    TransactionDate
FROM InventoryTransactions 
WHERE Notes LIKE '%Hoàn trả sản phẩm%'
ORDER BY TransactionDate DESC;

-- Bước 4: Kiểm tra tổng quan các loại giao dịch
SELECT 
    TransactionType,
    COUNT(*) as SoLuong,
    SUM(CASE WHEN QuantityChange > 0 THEN 1 ELSE 0 END) as SoLuongDuong,
    SUM(CASE WHEN QuantityChange < 0 THEN 1 ELSE 0 END) as SoLuongAm
FROM InventoryTransactions 
GROUP BY TransactionType
ORDER BY TransactionType;
