# Test QLDonHang - Tìm Kiếm Theo Tên Khách Hàng

## Chức Năng Đã Cập Nhật

Chức năng tìm kiếm trong QLDonHang đã được sửa để tìm theo **tên khách hàng** từ bảng `Addresses` thay vì tên user từ bảng `Users`. Giờ đây sẽ tìm kiếm gần đúng (fuzzy search) theo cột `CustomerName` trong bảng `Addresses`.

## Thay Đổi Đã Thực Hiện

### **1. Logic Tìm Kiếm Mới:**
```java
// Tìm kiếm theo tên khách hàng trong bảng Addresses
String sql = "SELECT DISTINCT o.* FROM Orders o " +
           "INNER JOIN Addresses a ON o.OrderID = a.OrderID " +
           "WHERE a.CustomerName LIKE ? " +
           "ORDER BY o.OrderID DESC";

result = poly.util.XQuery.getBeanList(Order.class, sql, "%" + keyword + "%");
```

### **2. Fallback Logic:**
- **Ưu tiên**: Tìm theo `OrderID` trong bảng `Addresses`
- **Fallback**: Tìm theo `DeliveryAddressID` nếu không tìm thấy theo `OrderID`

### **3. Cải Thiện UX:**
- Hiển thị thông báo kết quả tìm kiếm
- Xử lý lỗi với thông báo rõ ràng
- Tìm kiếm gần đúng với `LIKE %keyword%`

## Cách Test

### **Bước 1: Mở QLDonHang**
1. Vào Admin Panel
2. Chọn "Quản lý đơn hàng"
3. Màn hình sẽ load tất cả đơn hàng

### **Bước 2: Test Tìm Kiếm**
1. **Nhập tên khách hàng** vào ô tìm kiếm (jTextField1)
2. **Nhấn nút tìm kiếm** (jButton1)
3. **Quan sát kết quả** trong bảng và thông báo popup

### **Bước 3: Các Trường Hợp Test**

#### **Test Case 1: Tìm kiếm chính xác**
- **Input**: "Quang tets 14 lần 3"
- **Expected**: Hiển thị đơn hàng có tên khách hàng chính xác

#### **Test Case 2: Tìm kiếm một phần tên**
- **Input**: "Quang"
- **Expected**: Hiển thị tất cả đơn hàng có tên khách hàng chứa "Quang"

#### **Test Case 3: Tìm kiếm không có kết quả**
- **Input**: "Không tồn tại"
- **Expected**: Hiển thị thông báo "Không tìm thấy đơn hàng nào"

#### **Test Case 4: Tìm kiếm rỗng**
- **Input**: "" (để trống)
- **Expected**: Hiển thị tất cả đơn hàng

## Kết Quả Mong Đợi

### **✅ Thành Công:**
- Tìm kiếm theo tên khách hàng thực tế từ bảng Addresses
- Hiển thị thông báo kết quả rõ ràng
- Tìm kiếm gần đúng hoạt động tốt
- Fallback logic hoạt động khi cần thiết

### **❌ Thất Bại:**
- Vẫn tìm theo tên user thay vì tên khách hàng
- Không hiển thị thông báo kết quả
- Lỗi SQL hoặc Exception
- Không tìm thấy kết quả đúng

## Ví Dụ SQL Query

### **Query Chính (theo OrderID):**
```sql
SELECT DISTINCT o.* FROM Orders o 
INNER JOIN Addresses a ON o.OrderID = a.OrderID 
WHERE a.CustomerName LIKE '%Quang%' 
ORDER BY o.OrderID DESC
```

### **Query Fallback (theo DeliveryAddressID):**
```sql
SELECT DISTINCT o.* FROM Orders o 
INNER JOIN Addresses a ON o.DeliveryAddressID = a.AddressID 
WHERE a.CustomerName LIKE '%Quang%' 
ORDER BY o.OrderID DESC
```

## Troubleshooting

### **Nếu không tìm thấy kết quả:**

1. **Kiểm tra dữ liệu:**
   ```sql
   -- Kiểm tra bảng Addresses có dữ liệu
   SELECT OrderID, CustomerName FROM Addresses WHERE CustomerName LIKE '%Quang%';
   
   -- Kiểm tra bảng Orders
   SELECT OrderID, DeliveryAddressID FROM Orders;
   ```

2. **Kiểm tra console:**
   - Xem có lỗi SQL không
   - Kiểm tra debug output

3. **Kiểm tra logic:**
   - Đảm bảo OrderID trong Addresses khớp với Orders
   - Kiểm tra DeliveryAddressID có đúng không

### **Nếu vẫn tìm theo tên user:**

1. **Kiểm tra event handler:**
   - Đảm bảo `jButton1ActionPerformed` đã được cập nhật
   - Kiểm tra không có cache cũ

2. **Kiểm tra import:**
   - Đảm bảo đã import đúng các class cần thiết

## So Sánh Trước và Sau

### **Trước (Cũ):**
```java
// Tìm theo tên user từ bảng Users
List<User> users = poly.util.XQuery.getBeanList(User.class, 
    "SELECT * FROM Users WHERE FullName LIKE ?", "%" + keyword + "%");
for (User u : users) {
    result.addAll(orderDAO.selectByUserId(u.getUserId()));
}
```

### **Sau (Mới):**
```java
// Tìm theo tên khách hàng từ bảng Addresses
String sql = "SELECT DISTINCT o.* FROM Orders o " +
           "INNER JOIN Addresses a ON o.OrderID = a.OrderID " +
           "WHERE a.CustomerName LIKE ? " +
           "ORDER BY o.OrderID DESC";
result = poly.util.XQuery.getBeanList(Order.class, sql, "%" + keyword + "%");
```

## Kết Luận

Với cập nhật này, chức năng tìm kiếm trong QLDonHang giờ đây sẽ:
- ✅ Tìm theo tên khách hàng thực tế từ bảng Addresses
- ✅ Hỗ trợ tìm kiếm gần đúng (fuzzy search)
- ✅ Hiển thị thông báo kết quả rõ ràng
- ✅ Có logic fallback để đảm bảo tìm kiếm hiệu quả

Hãy test và cho tôi biết kết quả! 🚀
