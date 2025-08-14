# Test QLDonHang - Hiển Thị Địa Chỉ Đúng

## Vấn Đề Đã Sửa

Màn hình `QLDonHang` (Quản lý đơn hàng) trước đây hiển thị địa chỉ sai vì chỉ lấy theo `DeliveryAddressID`. Đã sửa để ưu tiên lấy địa chỉ theo `OrderID` từ bảng `Addresses`.

## Thay Đổi Đã Thực Hiện

### **Logic Mới Trong `fillToTableWithList()`:**

```java
// Ưu tiên lấy địa chỉ theo OrderID (cách mới)
Address address = null;
if (o.getOrderId() != null) {
    address = XQuery.getSingleBean(Address.class, "SELECT * FROM Addresses WHERE OrderID=?", o.getOrderId());
}

// Nếu không tìm thấy theo OrderID, thử theo DeliveryAddressID (cách cũ)
if (address == null && o.getDeliveryAddressId() != null) {
    address = XQuery.getSingleBean(Address.class, "SELECT * FROM Addresses WHERE AddressId=?", o.getDeliveryAddressId());
}
```

### **Cải Thiện Hiển Thị Địa Chỉ:**

- Sử dụng `StringBuilder` để tạo địa chỉ đầy đủ
- Kiểm tra null/empty cho từng phần địa chỉ
- Hiển thị thông báo khi không có thông tin địa chỉ

## Cách Test

### **Bước 1: Mở QLDonHang**
1. Vào Admin Panel
2. Chọn "Quản lý đơn hàng"
3. Màn hình sẽ load tất cả đơn hàng

### **Bước 2: Quan Sát Console Output**
Khi màn hình load, console sẽ hiển thị:

```
🔍 DEBUG QLDonHang - Tìm địa chỉ theo OrderID: 32
🔍 DEBUG QLDonHang - Tìm thấy địa chỉ theo OrderID: Quang tets 14 lần ...
🔍 DEBUG QLDonHang - Tìm địa chỉ theo OrderID: 31
🔍 DEBUG QLDonHang - Không tìm thấy địa chỉ theo OrderID
🔍 DEBUG QLDonHang - Tìm địa chỉ theo DeliveryAddressID: 33
🔍 DEBUG QLDonHang - Tìm thấy địa chỉ theo DeliveryAddressID: 456 Elm Street
```

### **Bước 3: Kiểm Tra Cột Địa Chỉ**
Trong bảng đơn hàng, cột "Địa chỉ giao hàng" sẽ hiển thị:

- **Đơn hàng 32**: "Quang tets 14 lần ..., TP Hà Nội, Việt Nam" (từ OrderID)
- **Đơn hàng 31**: "456 Elm Street, New York, USA" (từ DeliveryAddressID)
- **Đơn hàng khác**: "Không có thông tin địa chỉ" (nếu không tìm thấy)

## Kết Quả Mong Đợi

### **✅ Thành Công:**
- Đơn hàng 32 hiển thị địa chỉ thực tế "Quang tets 14 lần ..."
- Các đơn hàng khác hiển thị địa chỉ đúng từ bảng Addresses
- Console hiển thị debug info rõ ràng

### **❌ Thất Bại:**
- Vẫn hiển thị "Địa chỉ mặc định"
- Console không hiển thị debug info
- Địa chỉ hiển thị sai hoặc trống

## Troubleshooting

### **Nếu vẫn hiển thị sai:**

1. **Kiểm tra Console:**
   - Xem có debug output không
   - Kiểm tra OrderID có được tìm thấy không

2. **Kiểm tra Database:**
   ```sql
   -- Kiểm tra đơn hàng 32
   SELECT OrderID, DeliveryAddressID FROM Orders WHERE OrderID = 32;
   
   -- Kiểm tra địa chỉ có OrderID = 32
   SELECT AddressID, OrderID, AddressLine1 FROM Addresses WHERE OrderID = 32;
   ```

3. **Kiểm tra XQuery:**
   - Đảm bảo `XQuery.getSingleBean()` hoạt động đúng
   - Kiểm tra SQL query có trả về kết quả

### **Nếu không có debug output:**

1. **Kiểm tra import:**
   ```java
   import poly.util.XQuery;
   ```

2. **Kiểm tra method call:**
   - Đảm bảo `fillToTableWithList()` được gọi
   - Kiểm tra `o.getOrderId()` không null

## Kết Luận

Với thay đổi này, màn hình QLDonHang sẽ hiển thị địa chỉ đúng từ bảng Addresses, ưu tiên theo OrderID và fallback về DeliveryAddressID nếu cần thiết.

Hãy test và cho tôi biết kết quả! 🚀
