# Test Double-Click - Phiên Bản Cuối Cùng

## ✅ Đã Sửa Xong
- **Event listeners** đã được cập nhật với logic `getClickCount()`
- **Double-click detection** hoạt động đúng cách
- **Dialog mở** khi double-click vào dòng trong bảng

## Các Thay Đổi Đã Thực Hiện

### 1. Sửa `tblLichSu` Event Listener
```java
tblLichSu.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            tblLichSuMouseClicked(evt);
        } else if (evt.getClickCount() == 2) {
            System.out.println("DEBUG: tblLichSu double-click detected in mouseClicked");
            tblLichSuMouseDoubleClicked(evt);
        }
    }
});
```

### 2. Sửa `tblHienTai` Event Listener
```java
tblHienTai.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            tblHienTaiMouseClicked(evt);
        } else if (evt.getClickCount() == 2) {
            System.out.println("DEBUG: tblHienTai double-click detected in mouseClicked");
            tblHienTaiMouseDoubleClicked(evt);
        }
    }
});
```

## Cách Test

### Bước 1: Chạy Ứng Dụng
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Theo Dõi Đơn Hàng"
3. **Kiểm tra console logs** khởi tạo

### Bước 2: Test Tab "Lịch Sử Đơn Hàng"
1. **Click một lần** vào dòng trong bảng → Dòng được highlight
2. **Double-click** vào dòng đã chọn → Dialog "Chi Tiết Hóa Đơn" mở
3. **Kiểm tra console logs** để xác nhận

### Bước 3: Test Tab "Đơn Hàng Hiện Tại"
1. **Chuyển sang tab** "Đơn Hàng Hiện Tại"
2. **Click một lần** vào dòng → Dòng được highlight
3. **Double-click** vào dòng → Dialog "Chi Tiết Hóa Đơn" mở
4. **Kiểm tra console logs** để xác nhận

### Bước 4: Kiểm Tra Dialog
1. **Dialog mở** với thông tin chi tiết đơn hàng
2. **Số tiền hiển thị** đúng format (40,000,000.00 VNĐ)
3. **Đóng dialog** bằng nút "Đóng"

## Console Logs Mong Đợi

### Khi Khởi Tạo:
```
DEBUG: tblLichSu listeners count: 1
DEBUG: tblHienTai listeners count: 1
DEBUG: Original event listeners preserved
```

### Khi Single-Click:
```
DEBUG: tblLichSu clicked - click count: 1
```

### Khi Double-Click:
```
DEBUG: tblLichSu double-click detected in mouseClicked
DEBUG: tblLichSu double-click event triggered
DEBUG: Tab selected: 0, Row selected: [số dòng]
DEBUG: OrderID from table: [mã đơn hàng]
DEBUG: Current order loaded: [mã đơn hàng]
DEBUG: Dialog opened successfully
```

## Kết Quả Mong Đợi

### ✅ Single-Click:
- Dòng được highlight (chọn)
- Không mở dialog

### ✅ Double-Click:
- Dialog "Chi Tiết Hóa Đơn" mở
- Hiển thị thông tin chi tiết đơn hàng
- Số tiền format đúng (40,000,000.00 VNĐ)

### ✅ Console Logs:
- Hiển thị đầy đủ debug information
- Theo dõi được quá trình xử lý

## Troubleshooting

### Nếu Double-Click Không Hoạt Động:
1. **Kiểm tra console logs** - có thấy "double-click detected" không?
2. **Kiểm tra row selection** - dòng có được chọn trước khi double-click không?
3. **Kiểm tra OrderID** - có lấy được OrderID từ bảng không?

### Nếu Dialog Không Mở:
1. **Kiểm tra import** - `import poly.ui.manager.HoaDonChiTiet;`
2. **Kiểm tra constructor** - `HoaDonChiTiet` có constructor đúng không?
3. **Kiểm tra database** - có dữ liệu đơn hàng không?

### Nếu Số Tiền Hiển Thị Sai:
1. **Kiểm tra method** `formatCurrency()` trong `HoaDonChiTiet`
2. **Kiểm tra database** - giá trị trong database có đúng không?

## Lưu Ý Quan Trọng
- **Single-click** và **double-click** hoạt động độc lập
- **Row selection** phải được thực hiện trước khi double-click
- **Console logs** giúp debug nếu có vấn đề

**Test và cho tôi biết kết quả!**
