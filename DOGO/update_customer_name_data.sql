-- Script cập nhật CustomerName cho dữ liệu cũ
USE Storedogo2;

PRINT '=== CẬP NHẬT CUSTOMERNAME CHO DỮ LIỆU CŨ ===';

-- 1. Kiểm tra dữ liệu hiện tại
PRINT '=== BƯỚC 1: KIỂM TRA DỮ LIỆU HIỆN TẠI ===';
SELECT 
    'Addresses' as TableName,
    COUNT(*) as TotalRows,
    COUNT(CustomerName) as RowsWithCustomerName,
    COUNT(*) - COUNT(CustomerName) as RowsWithoutCustomerName
FROM Addresses
UNION ALL
SELECT 
    'Orders' as TableName,
    COUNT(*) as TotalRows,
    COUNT(DeliveryAddressID) as RowsWithDeliveryAddress,
    COUNT(*) - COUNT(DeliveryAddressID) as RowsWithoutDeliveryAddress
FROM Orders;

-- 2. Cập nhật CustomerName cho các địa chỉ chưa có
PRINT '=== BƯỚC 2: CẬP NHẬT CUSTOMERNAME ===';
UPDATE Addresses 
SET CustomerName = (
    SELECT u.FullName 
    FROM Users u 
    WHERE u.UserID = Addresses.UserID
)
WHERE CustomerName IS NULL OR CustomerName = '';

PRINT 'Đã cập nhật CustomerName cho các địa chỉ chưa có';

-- 3. Kiểm tra kết quả sau khi cập nhật
PRINT '=== BƯỚC 3: KIỂM TRA KẾT QUẢ SAU KHI CẬP NHẬT ===';
SELECT 
    'Addresses' as TableName,
    COUNT(*) as TotalRows,
    COUNT(CustomerName) as RowsWithCustomerName,
    COUNT(*) - COUNT(CustomerName) as RowsWithoutCustomerName
FROM Addresses;

-- 4. Hiển thị mẫu dữ liệu
PRINT '=== BƯỚC 4: HIỂN THỊ MẪU DỮ LIỆU ===';
SELECT TOP 10
    AddressID,
    UserID,
    CustomerName,
    AddressLine1,
    City,
    Country,
    Phone,
    IsDefault,
    CreatedDate
FROM Addresses
ORDER BY CreatedDate DESC;

-- 5. Test query để hiển thị đơn hàng với CustomerName
PRINT '=== BƯỚC 5: TEST QUERY HIỂN THỊ ĐƠN HÀNG VỚI CUSTOMERNAME ===';
SELECT TOP 5
    o.OrderID,
    o.UserID,
    a.CustomerName,
    u.FullName as UserFullName,
    o.OrderDate,
    o.TotalAmount,
    o.OrderStatus,
    a.AddressLine1 + ', ' + a.City + ', ' + a.Country as DeliveryAddress
FROM Orders o
LEFT JOIN Addresses a ON o.DeliveryAddressID = a.AddressID
LEFT JOIN Users u ON o.UserID = u.UserID
ORDER BY o.OrderDate DESC;

PRINT '=== HOÀN THÀNH CẬP NHẬT CUSTOMERNAME ===';
PRINT 'Bây giờ bảng QLDonHang sẽ hiển thị CustomerName thay vì tên user!';
