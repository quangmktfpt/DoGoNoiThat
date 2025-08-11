# Hướng Dẫn Debug Vấn Đề Double-Click

## Vấn Đề
Double-click vào tab "Lịch sử đơn hàng" trong `TDDonHangJDialog_nghia` không hiển thị chi tiết hóa đơn.

## Các Thay Đổi Đã Thực Hiện

### 1. Cải Thiện Method `openOrderDetailDialog()`
- Thêm debug logs để theo dõi quá trình
- Lấy OrderID trực tiếp từ bảng thay vì dựa vào `currentOrder`
- Thêm kiểm tra null và validation

### 2. Thêm Debug Logs
- `tblLichSuMouseDoubleClicked()`: Log khi event được trigger
- `tblHienTaiMouseDoubleClicked()`: Log khi event được trigger
- `openOrderDetailDialog()`: Log chi tiết quá trình xử lý

## Cách Debug

### Bước 1: Chạy Ứng Dụng
1. Chạy ứng dụng với debug mode
2. Mở màn hình "Theo Dõi Đơn Hàng"
3. Chuyển sang tab "Lịch Sử Đơn Hàng"

### Bước 2: Test Double-Click
1. **Chọn một dòng** trong bảng (click một lần)
2. **Double-click** vào dòng đã chọn
3. Kiểm tra console để xem debug logs

### Bước 3: Kiểm Tra Debug Logs
Bạn sẽ thấy các log sau trong console:

```
DEBUG: tblLichSu double-click event triggered
DEBUG: Tab selected: 0, Row selected: [số dòng]
DEBUG: OrderID from table: [mã đơn hàng]
DEBUG: Current order loaded: [mã đơn hàng]
DEBUG: Dialog opened successfully
```

## Các Trường Hợp Có Thể Xảy Ra

### Trường Hợp 1: Event Không Được Trigger
**Log:** Không có log "tblLichSu double-click event triggered"
**Nguyên nhân:** Event listener không được gắn đúng
**Giải pháp:** Kiểm tra lại code trong `initComponents()`

### Trường Hợp 2: Không Chọn Dòng
**Log:** `DEBUG: Tab selected: 0, Row selected: -1`
**Nguyên nhân:** Chưa click chọn dòng trước khi double-click
**Giải pháp:** Click một lần để chọn dòng trước khi double-click

### Trường Hợp 3: OrderID Null
**Log:** `DEBUG: OrderID from table: null`
**Nguyên nhân:** Dữ liệu trong bảng không đúng format
**Giải pháp:** Kiểm tra method `fillToTable()` và `fillCurrentOrdersTable()`

### Trường Hợp 4: Không Tìm Thấy Đơn Hàng
**Log:** `DEBUG: Current order loaded: null`
**Nguyên nhân:** OrderID không tồn tại trong database
**Giải pháp:** Kiểm tra dữ liệu trong bảng Orders

### Trường Hợp 5: Lỗi Mở Dialog
**Log:** `DEBUG: Error opening dialog: [lỗi]`
**Nguyên nhân:** Lỗi khi tạo `HoaDonChiTiet` dialog
**Giải pháp:** Kiểm tra constructor của `HoaDonChiTiet`

## Kiểm Tra Thêm

### 1. Kiểm Tra Dữ Liệu Bảng
```sql
-- Kiểm tra dữ liệu trong bảng Orders
SELECT TOP 10 * FROM Orders ORDER BY OrderDate DESC;

-- Kiểm tra dữ liệu trong bảng OrderDetails
SELECT TOP 10 * FROM OrderDetails ORDER BY OrderID DESC;
```

### 2. Kiểm Tra Method Fill Table
Xem method `fillToTable()` và `fillCurrentOrdersTable()` để đảm bảo:
- Dữ liệu được load đúng
- OrderID được đặt ở cột đầu tiên (index 0)
- Không có lỗi khi query database

### 3. Kiểm Tra HoaDonChiTiet
Đảm bảo class `HoaDonChiTiet` có constructor:
```java
public HoaDonChiTiet(java.awt.Frame parent, boolean modal, int orderId)
```

## Cách Sử Dụng Đúng

### 1. Chọn Dòng Trước
- **Click một lần** vào dòng muốn xem chi tiết
- Dòng sẽ được highlight (chọn)

### 2. Double-Click Để Mở
- **Double-click** vào dòng đã chọn
- Dialog chi tiết hóa đơn sẽ mở

### 3. Kiểm Tra Console
- Mở console/terminal để xem debug logs
- Nếu có lỗi, logs sẽ hiển thị chi tiết

## Troubleshooting

### Nếu Vẫn Không Hoạt Động
1. **Kiểm tra console logs** để xác định vấn đề
2. **Đảm bảo đã chọn dòng** trước khi double-click
3. **Kiểm tra dữ liệu** trong bảng có đúng không
4. **Test với tab "Đơn Hàng Hiện Tại"** để so sánh

### Nếu Có Lỗi Database
1. Kiểm tra connection string
2. Kiểm tra quyền truy cập database
3. Kiểm tra dữ liệu trong bảng Orders

### Nếu Có Lỗi UI
1. Kiểm tra import của `HoaDonChiTiet`
2. Kiểm tra constructor của `HoaDonChiTiet`
3. Kiểm tra parent frame của dialog

## Lưu Ý
- Đảm bảo chạy ứng dụng với debug mode để xem logs
- Kiểm tra console/terminal để theo dõi debug logs
- Test với cả 2 tab để so sánh hành vi
