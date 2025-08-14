-- =====================================================
-- DEBUG ĐƠN HÀNG 32 - KIỂM TRA DỮ LIỆU
-- =====================================================

-- 1. Kiểm tra đơn hàng 32
SELECT 
    'Orders Table' as Source,
    OrderID,
    UserID,
    DeliveryAddressID,
    PaymentMethod,
    OrderStatus,
    OrderDate
FROM Orders 
WHERE OrderID = 32;

-- 2. Kiểm tra địa chỉ có OrderID = 32
SELECT 
    'Addresses Table' as Source,
    AddressID,
    UserID,
    OrderID,
    AddressLine1,
    CustomerName,
    Phone,
    City,
    Country,
    IsDefault,
    CreatedDate
FROM Addresses 
WHERE OrderID = 32;

-- 3. Kiểm tra địa chỉ có DeliveryAddressID = (giá trị từ Orders)
SELECT 
    'Addresses by DeliveryAddressID' as Source,
    AddressID,
    UserID,
    OrderID,
    AddressLine1,
    CustomerName,
    Phone,
    City,
    Country,
    IsDefault,
    CreatedDate
FROM Addresses 
WHERE AddressID = (
    SELECT DeliveryAddressID 
    FROM Orders 
    WHERE OrderID = 32
);

-- 4. Kiểm tra tất cả địa chỉ của user (UserID từ Orders)
SELECT 
    'All Addresses for User' as Source,
    AddressID,
    UserID,
    OrderID,
    AddressLine1,
    CustomerName,
    Phone,
    City,
    Country,
    IsDefault,
    CreatedDate
FROM Addresses 
WHERE UserID = (
    SELECT UserID 
    FROM Orders 
    WHERE OrderID = 32
)
ORDER BY CreatedDate DESC;

-- 5. Kiểm tra liên kết giữa Orders và Addresses
SELECT 
    'Orders-Addresses Join' as Source,
    o.OrderID,
    o.UserID,
    o.DeliveryAddressID,
    a.AddressID,
    a.OrderID as Address_OrderID,
    a.AddressLine1,
    a.CustomerName,
    a.Phone,
    a.City,
    a.Country
FROM Orders o
LEFT JOIN Addresses a ON o.OrderID = a.OrderID
WHERE o.OrderID = 32;

-- 6. Kiểm tra tất cả địa chỉ có OrderID
SELECT 
    'All Addresses with OrderID' as Source,
    AddressID,
    UserID,
    OrderID,
    AddressLine1,
    CustomerName,
    Phone,
    City,
    Country,
    IsDefault,
    CreatedDate
FROM Addresses 
WHERE OrderID IS NOT NULL
ORDER BY OrderID DESC;

-- 7. Kiểm tra cấu trúc bảng Addresses
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses'
ORDER BY ORDINAL_POSITION;
