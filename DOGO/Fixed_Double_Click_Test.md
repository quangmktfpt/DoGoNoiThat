# Fix Double-Click - Sử Dụng Click Count

## Vấn Đề Đã Khắc Phục
- `MouseAdapter` không có method `mouseDoubleClicked`
- Đã sửa để sử dụng `mouseClicked` với `getClickCount()`
- Xử lý cả single-click (count=1) và double-click (count=2)

## Các Thay Đổi
1. **Sửa `tblLichSu` event listener:**
   ```java
   if (evt.getClickCount() == 1) {
       tblLichSuMouseClicked(evt);
   } else if (evt.getClickCount() == 2) {
       tblLichSuMouseDoubleClicked(evt);
   }
   ```

2. **Sửa `tblHienTai` event listener:**
   ```java
   if (evt.getClickCount() == 1) {
       tblHienTaiMouseClicked(evt);
   } else if (evt.getClickCount() == 2) {
       tblHienTaiMouseDoubleClicked(evt);
   }
   ```

## Cách Test

### Bước 1: Chạy Ứng Dụng
1. Chạy ứng dụng
2. Mở màn hình "Theo Dõi Đơn Hàng"
3. Kiểm tra console logs khởi tạo

### Bước 2: Test Single-Click
1. **Click một lần** vào dòng trong bảng
2. Dòng sẽ được highlight (chọn)
3. Kiểm tra console logs

### Bước 3: Test Double-Click
1. **Chọn dòng** (click một lần)
2. **Double-click** vào dòng đã chọn
3. Kiểm tra console logs

### Bước 4: Kiểm Tra Logs
**Single-click:**
```
DEBUG: tblLichSu clicked - click count: 1
```

**Double-click:**
```
DEBUG: tblLichSu double-click detected in mouseClicked
DEBUG: tblLichSu double-click event triggered
DEBUG: Tab selected: 0, Row selected: [số dòng]
DEBUG: OrderID from table: [mã đơn hàng]
DEBUG: Current order loaded: [mã đơn hàng]
DEBUG: Dialog opened successfully
```

## Kết Quả Mong Đợi
- ✅ **Single-click hoạt động** - Chọn dòng bình thường
- ✅ **Double-click hoạt động** - Mở dialog chi tiết
- ✅ **Debug logs hiển thị** - Theo dõi được quá trình

## Nếu Vẫn Có Vấn Đề
1. **Không thấy logs double-click** → Event không được trigger
2. **Thấy logs nhưng không mở dialog** → Lỗi trong `openOrderDetailDialog()`
3. **Dialog mở nhưng trống** → Lỗi trong `HoaDonChiTiet`

**Test và cho tôi biết kết quả!**
