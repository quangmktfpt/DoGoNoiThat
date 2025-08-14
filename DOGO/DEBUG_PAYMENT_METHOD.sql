-- Debug Payment Method Values
-- Kiểm tra giá trị thực tế của PaymentMethod trong database

-- Kiểm tra đơn hàng 32 (như trong ảnh)
SELECT 
    OrderID,
    PaymentMethod,
    LEN(PaymentMethod) as Length,
    ASCII(LEFT(PaymentMethod, 1)) as FirstCharASCII
FROM Orders 
WHERE OrderID = 32;

-- Kiểm tra tất cả các giá trị PaymentMethod khác nhau
SELECT DISTINCT 
    PaymentMethod,
    LEN(PaymentMethod) as Length,
    COUNT(*) as Count
FROM Orders 
WHERE PaymentMethod IS NOT NULL
GROUP BY PaymentMethod, LEN(PaymentMethod)
ORDER BY PaymentMethod;

-- Kiểm tra các đơn hàng có PaymentMethod chứa "Thanh toán"
SELECT 
    OrderID,
    PaymentMethod,
    LEN(PaymentMethod) as Length
FROM Orders 
WHERE PaymentMethod LIKE '%Thanh toán%'
ORDER BY OrderID;

-- Kiểm tra các đơn hàng có PaymentMethod chứa "COD"
SELECT 
    OrderID,
    PaymentMethod,
    LEN(PaymentMethod) as Length
FROM Orders 
WHERE PaymentMethod LIKE '%COD%'
ORDER BY OrderID;

-- Kiểm tra các đơn hàng có PaymentMethod chứa "Cash"
SELECT 
    OrderID,
    PaymentMethod,
    LEN(PaymentMethod) as Length
FROM Orders 
WHERE PaymentMethod LIKE '%Cash%'
ORDER BY OrderID;

-- Kiểm tra encoding của PaymentMethod
SELECT 
    OrderID,
    PaymentMethod,
    CAST(PaymentMethod AS VARBINARY(MAX)) as BinaryValue
FROM Orders 
WHERE OrderID = 32;
