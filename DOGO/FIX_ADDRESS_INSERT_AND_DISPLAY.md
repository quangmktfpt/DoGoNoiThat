# Fix Address Insert và Display Issues - Hướng dẫn Test

## Vấn đề đã sửa

### 🔴 **Vấn đề 1: NullPointerException khi tạo địa chỉ**
- **Lỗi**: `Cannot invoke "java.lang.Integer.intValue()" because the return value of "poly.entity.Address.getAddressId()" is null`
- **Nguyên nhân**: Method `insert()` trong `AddressDAOImpl` không trả về `AddressID` vừa tạo
- **Giải pháp**: Thêm logic để lấy `AddressID` vừa tạo và set vào object

### 🔴 **Vấn đề 2: Column 'CreatedDate' not found**
- **Lỗi**: `Column 'CreatedDate' not found!` trong `QLDonHang`
- **Nguyên nhân**: Sử dụng `XQuery.getSingleBean()` với SQL query có vấn đề mapping
- **Giải pháp**: Thay thế bằng `AddressDAO` methods

## Thay đổi đã thực hiện

### 1. AddressDAOImpl.java
- ✅ **Sửa** method `insert()` để lấy `AddressID` vừa tạo
- ✅ **Thêm** logic query `AddressID` và set vào object
- ✅ **Thêm** debug log để theo dõi

### 2. QLDonHang.java
- ✅ **Thay thế** `XQuery.getSingleBean()` bằng `AddressDAO` methods
- ✅ **Thêm** khai báo `AddressDAO` trong `fillToTableWithList()`
- ✅ **Sử dụng** `addressDAO.selectByOrderId()` và `addressDAO.selectById()`

## Cách Test

### Bước 1: Test Đặt Hàng Mới
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

### Bước 2: Kiểm tra Console Log
Khi ấn xác nhận, console sẽ hiển thị:
```
✓ OrderRequestDAOImpl: Đã tạo đơn hàng với OrderID: [số]
✓ OrderRequestDAOImpl: Để DatHangJDialog xử lý việc tạo địa chỉ giao hàng
✓ Đã tạo địa chỉ với AddressID: [số]
✓ DatHangJDialog: Đã tạo địa chỉ giao hàng thành công
  - OrderID: [số]
  - AddressID: [số]
  - Địa chỉ: 123 Test Street
  - Khách hàng: Test Customer
  - Số điện thoại: 0123456789
```

### Bước 3: Kiểm tra Database
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

### Bước 4: Test Hiển thị trong QLDonHang
1. **Vào màn hình "Quản lý đơn hàng"** (`QLDonHang`)
2. **Kiểm tra** không có lỗi "Column 'CreatedDate' not found"
3. **Kiểm tra** địa chỉ hiển thị đúng

### Bước 5: Test Hiển thị trong TDDonHangJDialog_nghia
1. **Vào màn hình "Theo dõi đơn hàng"** (`TDDonHangJDialog_nghia`)
2. **Chọn đơn hàng vừa tạo**
3. **Ấn "Xem chi tiết"**
4. **Kiểm tra** thông tin địa chỉ giao hàng có đúng không

## Kết quả mong đợi
- ✅ **Không còn NullPointerException** khi tạo địa chỉ
- ✅ **Không còn lỗi "Column 'CreatedDate' not found"**
- ✅ **Chỉ có 1 record** trong bảng `Addresses` cho mỗi đơn hàng
- ✅ **OrderID** và **DeliveryAddressID** khớp nhau
- ✅ **Thông tin địa chỉ** hiển thị đúng trong tất cả màn hình

## Nếu vẫn có vấn đề
1. **Kiểm tra console log** để xem có lỗi gì không
2. **Kiểm tra database** bằng các query trên
3. **Báo cáo lỗi** với thông tin chi tiết
