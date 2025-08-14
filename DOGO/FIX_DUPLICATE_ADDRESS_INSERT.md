# Fix Duplicate Address Insert - Hướng dẫn Test

## Vấn đề đã sửa
- **Trước**: Khi ấn "Xác nhận đơn hàng" trong `DatHangJDialog`, hệ thống tạo **2 lần** địa chỉ:
  1. `OrderRequestDAOImpl.createDeliveryAddress()` - tạo địa chỉ **KHÔNG có OrderID**
  2. `DatHangJDialog.confirmOrder()` - tạo địa chỉ **CÓ OrderID**
- **Sau**: Chỉ có **1 lần** tạo địa chỉ trong `DatHangJDialog.confirmOrder()`

## Thay đổi đã thực hiện

### 1. OrderRequestDAOImpl.java
- ✅ **Xóa** method `createDeliveryAddress()`
- ✅ **Sửa** `insert()` method để chỉ tạo đơn hàng, không tạo địa chỉ
- ✅ **Thêm** debug log để theo dõi luồng xử lý

### 2. DatHangJDialog.java  
- ✅ **Giữ nguyên** logic tạo địa chỉ trong `confirmOrder()`
- ✅ **Thêm** cập nhật `DeliveryAddressID` trong bảng `Orders`
- ✅ **Cải thiện** debug log

## Cách Test

### Bước 1: Cleanup Database (Chạy 1 lần)
```sql
-- Xóa các record địa chỉ dư thừa
DELETE FROM Addresses WHERE OrderID IS NULL;
```

### Bước 2: Test Đặt Hàng Mới
1. **Đăng nhập** vào hệ thống
2. **Thêm sản phẩm** vào giỏ hàng
3. **Vào màn hình đặt hàng** (`DatHangJDialog`)
4. **Điền thông tin giao hàng**:
   - Tên khách hàng: `Test Customer`
   - Số điện thoại: `0123456789`
   - Địa chỉ: `123 Test Street`
   - Thành phố: `Test City`
   - Quốc gia: `Việt Nam`
5. **Chọn phương thức thanh toán**: `Thanh toán khi nhận hàng`
6. **Ấn "Xác nhận đơn hàng"**

### Bước 3: Kiểm tra Console Log
Khi ấn xác nhận, console sẽ hiển thị:
```
✓ OrderRequestDAOImpl: Đã tạo đơn hàng với OrderID: [số]
✓ OrderRequestDAOImpl: Để DatHangJDialog xử lý việc tạo địa chỉ giao hàng
✓ DatHangJDialog: Đã tạo địa chỉ giao hàng thành công
  - OrderID: [số]
  - AddressID: [số]
  - Địa chỉ: 123 Test Street
  - Khách hàng: Test Customer
  - Số điện thoại: 0123456789
```

### Bước 4: Kiểm tra Database
```sql
-- Kiểm tra bảng Orders
SELECT OrderID, UserID, DeliveryAddressID, OrderDate 
FROM Orders 
WHERE OrderDate >= GETDATE() - 1
ORDER BY OrderID DESC;

-- Kiểm tra bảng Addresses
SELECT AddressID, UserID, CustomerName, AddressLine1, City, OrderID, CreatedDate
FROM Addresses 
WHERE CreatedDate >= GETDATE() - 1
ORDER BY AddressID DESC;
```

### Bước 5: Kiểm tra Hiển thị Địa chỉ
1. **Vào màn hình "Xem chi tiết đơn hàng"** (`TDDonHangJDialog_nghia`)
2. **Chọn đơn hàng vừa tạo**
3. **Ấn "Xem chi tiết"**
4. **Kiểm tra** thông tin địa chỉ giao hàng có đúng không

## Kết quả mong đợi
- ✅ **Chỉ có 1 record** trong bảng `Addresses` cho mỗi đơn hàng
- ✅ **OrderID** trong bảng `Addresses` khớp với `OrderID` trong bảng `Orders`
- ✅ **DeliveryAddressID** trong bảng `Orders` khớp với `AddressID` trong bảng `Addresses`
- ✅ **Thông tin địa chỉ** hiển thị đúng trong màn hình xem chi tiết

## Nếu vẫn có vấn đề
1. **Kiểm tra console log** để xem có lỗi gì không
2. **Kiểm tra database** bằng các query trên
3. **Báo cáo lỗi** với thông tin chi tiết
