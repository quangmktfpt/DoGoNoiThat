# Fix Hiển thị Tên User - Hướng dẫn Test

## Vấn đề đã sửa
- **Trước**: Màn hình "Lịch sử đơn hàng" hiển thị "N/A" ở cột "Người Đặt"
- **Sau**: Hiển thị **tên user** (từ bảng `Users`) thay vì tên khách hàng thực tế

## Thay đổi đã thực hiện

### TDDonHangJDialog_nghia.java
- ✅ **Sửa** method `getRecipientName()` để lấy tên từ bảng `Users`
- ✅ **Thay đổi** logic từ `DeliveryAddressID` sang `UserID`
- ✅ **Cải thiện** error handling và fallback

## Cách Test

### Bước 1: Kiểm tra Database
```sql
-- Kiểm tra bảng Users
SELECT UserID, FullName, Email 
FROM Users 
ORDER BY UserID;

-- Kiểm tra bảng Orders
SELECT OrderID, UserID, OrderDate, OrderStatus 
FROM Orders 
ORDER BY OrderID DESC;
```

### Bước 2: Test Hiển thị Tên User
1. **Đăng nhập** vào hệ thống với user có tên rõ ràng
2. **Vào màn hình "Theo dõi đơn hàng"** (`TDDonHangJDialog_nghia`)
3. **Chọn tab "Lịch sử đơn hàng"**
4. **Kiểm tra** cột "Người Đặt" có hiển thị tên user không

### Bước 3: Test với User Khác
1. **Đăng xuất** và đăng nhập với user khác
2. **Kiểm tra** tên hiển thị có đúng không
3. **So sánh** với dữ liệu trong bảng `Users`

## Kết quả mong đợi
- ✅ **Cột "Người Đặt"** hiển thị tên user từ bảng `Users`
- ✅ **Không còn "N/A"** ở cột này
- ✅ **Tên hiển thị** khớp với `FullName` trong bảng `Users`

## Nếu vẫn có vấn đề
1. **Kiểm tra** bảng `Users` có dữ liệu `FullName` không
2. **Kiểm tra** `UserID` trong bảng `Orders` có khớp với `Users` không
3. **Báo cáo lỗi** với thông tin chi tiết
