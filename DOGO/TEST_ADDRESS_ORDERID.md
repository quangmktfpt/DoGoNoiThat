# Test Logic Lấy Địa Chỉ Theo OrderID

## Tình Huống Test

Dựa trên dữ liệu database hiện tại:
- **OrderID**: 32
- **AddressID**: 34 
- **AddressLine1**: "Quang tets 14 lần ..."
- **CustomerName**: "Quang tets 14 lần ..."
- **Phone**: (có số điện thoại)
- **City**: "TP Hà Nội"
- **Country**: "Việt Nam"

## Các Bước Test

### 1. **Kiểm Tra Database**
```sql
-- Kiểm tra đơn hàng 32
SELECT * FROM Orders WHERE OrderID = 32;

-- Kiểm tra địa chỉ có OrderID = 32
SELECT * FROM Addresses WHERE OrderID = 32;

-- Kiểm tra liên kết
SELECT 
    o.OrderID,
    o.DeliveryAddressID,
    a.AddressID,
    a.AddressLine1,
    a.CustomerName,
    a.Phone,
    a.City,
    a.Country
FROM Orders o
LEFT JOIN Addresses a ON o.OrderID = a.OrderID
WHERE o.OrderID = 32;
```

### 2. **Test Logic Trong Code**

Khi nhấn "Xem Chi Tiết" cho đơn hàng 32:

```java
// Logic sẽ chạy như sau:
if (currentOrder.getOrderId() != null) { // OrderID = 32
    address = addressDAO.selectByOrderId(currentOrder.getOrderId()); // Tìm AddressID = 34
}

// Kết quả mong đợi:
// - address != null
// - address.getAddressLine1() = "Quang tets 14 lần ..."
// - address.getCustomerName() = "Quang tets 14 lần ..."
// - address.getPhone() = (số điện thoại thực tế)
// - address.getCity() = "TP Hà Nội"
// - address.getCountry() = "Việt Nam"
```

### 3. **Debug Output Mong Đợi**

Khi chạy, console sẽ hiển thị:
```
DEBUG - OrderID: 32
DEBUG - DeliveryAddressID: (có thể null hoặc có giá trị)
DEBUG - Address found: true
DEBUG - AddressLine1: Quang tets 14 lần ...
DEBUG - CustomerName: Quang tets 14 lần ...
```

### 4. **Kết Quả Hiển Thị Mong Đợi**

Trong dialog "Xem Chi Tiết":
```
=== THÔNG TIN GIAO HÀNG ===
Họ tên người nhận: Quang tets 14 lần ...
Số điện thoại: (số điện thoại thực tế)
Địa chỉ: Quang tets 14 lần ...
Thành phố: TP Hà Nội
Quốc gia: Việt Nam
```

## Các Trường Hợp Test Khác

### **Trường Hợp 1: Đơn hàng có OrderID nhưng không có địa chỉ**
```sql
-- Tạo đơn hàng test không có địa chỉ
INSERT INTO Orders (UserID, OrderDate, TotalAmount, PaymentMethod, OrderStatus, IsActive)
VALUES (4, GETDATE(), 1000000, 'Thanh toán khi nhận hàng', 'Pending', 1);
```

### **Trường Hợp 2: Đơn hàng cũ (chỉ có DeliveryAddressID)**
```sql
-- Kiểm tra đơn hàng cũ
SELECT * FROM Orders WHERE DeliveryAddressID IS NOT NULL AND OrderID NOT IN (
    SELECT DISTINCT OrderID FROM Addresses WHERE OrderID IS NOT NULL
);
```

## Cách Thực Hiện Test

### **Bước 1: Chạy SQL kiểm tra**
```sql
-- Kiểm tra dữ liệu hiện tại
SELECT 
    'Orders' as Table_Name,
    OrderID,
    DeliveryAddressID,
    PaymentMethod,
    OrderStatus
FROM Orders 
WHERE OrderID = 32

UNION ALL

SELECT 
    'Addresses' as Table_Name,
    OrderID,
    AddressID,
    AddressLine1,
    CustomerName
FROM Addresses 
WHERE OrderID = 32;
```

### **Bước 2: Test trong ứng dụng**
1. Mở màn hình theo dõi đơn hàng
2. Tìm đơn hàng có OrderID = 32
3. Nhấn "Xem Chi Tiết"
4. Kiểm tra thông tin địa chỉ hiển thị

### **Bước 3: Kiểm tra console**
Xem debug output trong console để đảm bảo:
- OrderID được tìm thấy
- Address được tìm thấy
- Thông tin địa chỉ đúng

## Kết Quả Mong Đợi

✅ **Thành công**: Hiển thị địa chỉ thực tế "Quang tets 14 lần ..." thay vì "Địa chỉ mặc định"

❌ **Thất bại**: Vẫn hiển thị "Địa chỉ mặc định" hoặc thông tin không đúng

## Troubleshooting

### **Nếu vẫn hiển thị "Địa chỉ mặc định":**

1. **Kiểm tra OrderID trong Orders:**
```sql
SELECT OrderID, DeliveryAddressID FROM Orders WHERE OrderID = 32;
```

2. **Kiểm tra Addresses có OrderID:**
```sql
SELECT AddressID, OrderID, AddressLine1 FROM Addresses WHERE OrderID = 32;
```

3. **Kiểm tra logic trong code:**
- Đảm bảo `currentOrder.getOrderId()` trả về 32
- Đảm bảo `addressDAO.selectByOrderId(32)` trả về địa chỉ đúng

4. **Kiểm tra debug output:**
- Xem console có hiển thị debug info không
- Kiểm tra `Address found: true/false`

### **Nếu không tìm thấy địa chỉ:**

1. **Kiểm tra SQL query:**
```sql
SELECT * FROM Addresses WHERE OrderID = 32;
```

2. **Kiểm tra AddressDAO:**
- Đảm bảo `SELECT_BY_ORDER_SQL` đúng
- Đảm bảo `selectByOrderId()` hoạt động

3. **Kiểm tra Entity Address:**
- Đảm bảo `getOrderId()` và `setOrderId()` hoạt động
- Đảm bảo `selectBySql()` map đúng dữ liệu

## Kết Luận

Với dữ liệu hiện tại (OrderID 32 → AddressID 34), logic sẽ hoạt động đúng và hiển thị địa chỉ thực tế thay vì "Địa chỉ mặc định". 

Nếu vẫn có vấn đề, hãy kiểm tra debug output trong console để xác định nguyên nhân cụ thể.
