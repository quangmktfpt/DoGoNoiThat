-- Cleanup Duplicate Addresses
-- Xóa các record địa chỉ dư thừa có OrderID = NULL

-- Bước 1: Kiểm tra các địa chỉ có OrderID = NULL
SELECT 
    AddressID,
    UserID,
    CustomerName,
    AddressLine1,
    City,
    Phone,
    CreatedDate,
    OrderID
FROM Addresses 
WHERE OrderID IS NULL
ORDER BY CreatedDate DESC;

-- Bước 2: Kiểm tra các địa chỉ có OrderID (để so sánh)
SELECT 
    AddressID,
    UserID,
    CustomerName,
    AddressLine1,
    City,
    Phone,
    CreatedDate,
    OrderID
FROM Addresses 
WHERE OrderID IS NOT NULL
ORDER BY CreatedDate DESC;

-- Bước 3: Tìm các cặp địa chỉ trùng lặp (cùng thông tin nhưng khác OrderID)
SELECT 
    a1.AddressID as AddressID1,
    a1.OrderID as OrderID1,
    a2.AddressID as AddressID2,
    a2.OrderID as OrderID2,
    a1.CustomerName,
    a1.AddressLine1,
    a1.City,
    a1.Phone,
    a1.CreatedDate
FROM Addresses a1
INNER JOIN Addresses a2 ON 
    a1.UserID = a2.UserID AND
    a1.CustomerName = a2.CustomerName AND
    a1.AddressLine1 = a2.AddressLine1 AND
    a1.City = a2.City AND
    a1.Phone = a2.Phone AND
    a1.AddressID != a2.AddressID
WHERE 
    (a1.OrderID IS NULL AND a2.OrderID IS NOT NULL) OR
    (a1.OrderID IS NOT NULL AND a2.OrderID IS NULL)
ORDER BY a1.CreatedDate DESC;

-- Bước 4: Xóa các địa chỉ có OrderID = NULL (dư thừa)
-- CHÚ Ý: Chỉ chạy sau khi đã kiểm tra kỹ lưỡng!
DELETE FROM Addresses 
WHERE OrderID IS NULL;

-- Bước 5: Kiểm tra kết quả sau khi xóa
SELECT 
    AddressID,
    UserID,
    CustomerName,
    AddressLine1,
    City,
    Phone,
    CreatedDate,
    OrderID
FROM Addresses 
ORDER BY CreatedDate DESC;

-- Bước 6: Kiểm tra đơn hàng có DeliveryAddressID = NULL
SELECT 
    OrderID,
    UserID,
    OrderDate,
    TotalAmount,
    DeliveryAddressID
FROM Orders 
WHERE DeliveryAddressID IS NULL
ORDER BY OrderDate DESC;

-- Bước 7: Cập nhật DeliveryAddressID cho các đơn hàng bị thiếu
-- (Chỉ chạy nếu cần thiết)
UPDATE Orders 
SET DeliveryAddressID = (
    SELECT TOP 1 AddressID 
    FROM Addresses 
    WHERE Addresses.OrderID = Orders.OrderID
)
WHERE DeliveryAddressID IS NULL;
