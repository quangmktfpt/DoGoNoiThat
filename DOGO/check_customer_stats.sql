-- Script kiểm tra dữ liệu thống kê khách hàng
-- Thay thế [USER_ID] bằng ID khách hàng thực tế

-- 1. Kiểm tra thông tin khách hàng
SELECT 
    UserID,
    Username,
    FullName,
    Email
FROM Users 
WHERE Username = 'quangnn1';  -- Thay bằng username thực tế

-- 2. Kiểm tra số đơn hàng gần đây (30 ngày qua)
SELECT 
    COUNT(*) as RecentOrdersCount,
    'Đơn hàng trong 30 ngày qua' as Description
FROM Orders 
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'quangnn1')
AND OrderDate >= DATEADD(day, -30, GETDATE());

-- 3. Kiểm tra tổng chi tiêu (đơn hàng Completed)
SELECT 
    ISNULL(SUM(TotalAmount), 0) as TotalSpent,
    'Tổng chi tiêu (Completed)' as Description
FROM Orders 
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'quangnn1')
AND OrderStatus = 'Completed';

-- 4. Chi tiết các đơn hàng gần đây
SELECT 
    OrderID,
    OrderDate,
    OrderStatus,
    TotalAmount,
    CustomerName
FROM Orders 
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'quangnn1')
AND OrderDate >= DATEADD(day, -30, GETDATE())
ORDER BY OrderDate DESC;

-- 5. Chi tiết các đơn hàng Completed
SELECT 
    OrderID,
    OrderDate,
    TotalAmount,
    CustomerName
FROM Orders 
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'quangnn1')
AND OrderStatus = 'Completed'
ORDER BY OrderDate DESC;

-- 6. Thống kê theo trạng thái đơn hàng
SELECT 
    OrderStatus,
    COUNT(*) as OrderCount,
    ISNULL(SUM(TotalAmount), 0) as TotalAmount
FROM Orders 
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'quangnn1')
GROUP BY OrderStatus
ORDER BY OrderCount DESC;
