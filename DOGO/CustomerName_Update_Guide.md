# Hướng Dẫn Cập Nhật Sử Dụng CustomerName

## Tổng Quan
Đã cập nhật hệ thống để sử dụng cột `CustomerName` trong bảng `Addresses` thay vì tên user từ bảng `Users` để hiển thị tên khách hàng trong bảng QLDonHang.

## Các Thay Đổi Đã Thực Hiện

### 1. Cập Nhật QLDonHang.java
**File:** `src/main/java/poly/ui/manager/QLDonHang.java`
**Thay đổi:** Method `fillToTableWithList()`

**Trước:**
```java
// Lấy tên khách hàng
User user = userDAO.selectById(o.getUserId());
if (user != null) tenKhachHang = user.getFullName();
```

**Sau:**
```java
// Lấy tên khách hàng từ CustomerName trong Addresses
Address address = XQuery.getSingleBean(Address.class, "SELECT * FROM Addresses WHERE AddressId=?", o.getDeliveryAddressId());
if (address != null) {
    // Ưu tiên sử dụng CustomerName, nếu không có thì dùng tên user
    if (address.getCustomerName() != null && !address.getCustomerName().trim().isEmpty()) {
        tenKhachHang = address.getCustomerName();
    } else {
        // Fallback: lấy tên từ bảng Users
        User user = userDAO.selectById(o.getUserId());
        if (user != null) tenKhachHang = user.getFullName();
    }
    diaChiGiaoHang = address.getAddressLine1() + ", " + address.getCity() + ", " + address.getCountry();
} else {
    // Fallback: lấy tên từ bảng Users nếu không có address
    User user = userDAO.selectById(o.getUserId());
    if (user != null) tenKhachHang = user.getFullName();
}
```

### 2. Hệ Thống Đã Có Sẵn
- ✅ **Address Entity:** Đã có field `CustomerName`
- ✅ **AddressDAOImpl:** Đã có SQL insert/update `CustomerName`
- ✅ **OrderRequestDAOImpl:** Đã insert `CustomerName` khi tạo đơn hàng
- ✅ **DatHangJDialog:** Đã lưu `CustomerName` khi khách hàng đặt hàng

## Cách Hoạt Động

### 1. Khi Khách Hàng Đặt Hàng
1. Khách hàng điền thông tin trong form đặt hàng
2. Hệ thống lưu `CustomerName` vào bảng `Addresses`
3. Đơn hàng được tạo với `DeliveryAddressID` liên kết đến địa chỉ có `CustomerName`

### 2. Khi Admin Xem QLDonHang
1. Hệ thống lấy `DeliveryAddressID` từ đơn hàng
2. Query bảng `Addresses` để lấy `CustomerName`
3. Hiển thị `CustomerName` thay vì tên user
4. Fallback về tên user nếu không có `CustomerName`

## Các Bước Thực Hiện

### Bước 1: Chạy Script Cập Nhật Dữ Liệu
```sql
-- Chạy file: update_customer_name_data.sql
-- Script này sẽ:
-- 1. Kiểm tra dữ liệu hiện tại
-- 2. Cập nhật CustomerName cho các địa chỉ chưa có
-- 3. Hiển thị kết quả sau khi cập nhật
```

### Bước 2: Test Chức Năng
1. **Đặt hàng mới:** Khách hàng đặt hàng và kiểm tra `CustomerName` được lưu
2. **Xem QLDonHang:** Admin xem bảng QLDonHang và kiểm tra hiển thị `CustomerName`

### Bước 3: Kiểm Tra Dữ Liệu
```sql
-- Kiểm tra dữ liệu sau khi cập nhật
SELECT TOP 10
    AddressID,
    UserID,
    CustomerName,
    AddressLine1,
    City,
    Country
FROM Addresses
ORDER BY CreatedDate DESC;
```

## Lợi Ích

### 1. Linh Hoạt Hơn
- Khách hàng có thể đặt hàng với tên khác tên đăng ký
- Hỗ trợ đặt hàng cho người khác (quà tặng, đặt hộ)

### 2. Chính Xác Hơn
- Hiển thị đúng tên người nhận hàng
- Không phụ thuộc vào tên trong tài khoản

### 3. Tương Thích Ngược
- Vẫn hiển thị tên user nếu không có `CustomerName`
- Không ảnh hưởng đến dữ liệu cũ

## Troubleshooting

### Vấn Đề 1: CustomerName Vẫn NULL
**Nguyên nhân:** Dữ liệu cũ chưa được cập nhật
**Giải pháp:** Chạy script `update_customer_name_data.sql`

### Vấn Đề 2: Hiển Thị Tên User Thay Vì CustomerName
**Nguyên nhân:** Đơn hàng không có `DeliveryAddressID` hoặc address không có `CustomerName`
**Giải pháp:** Kiểm tra dữ liệu trong bảng `Addresses` và `Orders`

### Vấn Đề 3: Lỗi Khi Đặt Hàng
**Nguyên nhân:** Có thể do constraint hoặc lỗi database
**Giải pháp:** Kiểm tra log và đảm bảo database connection

## Kiểm Tra Kết Quả

### 1. Trong Database
```sql
-- Kiểm tra CustomerName đã được cập nhật
SELECT COUNT(*) as TotalAddresses,
       COUNT(CustomerName) as WithCustomerName,
       COUNT(*) - COUNT(CustomerName) as WithoutCustomerName
FROM Addresses;
```

### 2. Trong Ứng Dụng
- Mở QLDonHang và kiểm tra cột "Tên Khách Hàng"
- Đặt hàng mới và kiểm tra `CustomerName` được lưu
- Xem chi tiết đơn hàng để kiểm tra thông tin

## Lưu Ý
- Đảm bảo backup database trước khi chạy script cập nhật
- Test đầy đủ chức năng đặt hàng và xem đơn hàng
- Kiểm tra dữ liệu sau khi cập nhật để đảm bảo chính xác
