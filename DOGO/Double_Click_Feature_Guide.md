# Hướng Dẫn Chức Năng Double-Click Xem Chi Tiết Hóa Đơn

## Tính Năng Mới
Đã thêm chức năng **double-click** vào màn hình "Theo Dõi Đơn Hàng" (`TDDonHangJDialog_nghia`) để mở bảng chi tiết hóa đơn.

## Cách Sử Dụng

### 1. Mở Màn Hình Theo Dõi Đơn Hàng
- Vào màn hình "Theo Dõi Đơn Hàng" từ menu chính
- Chọn tab "Lịch Sử Đơn Hàng" hoặc "Đơn Hàng Hiện Tại"

### 2. Double-Click Để Xem Chi Tiết
- **Chọn một dòng** trong bảng (click một lần để chọn)
- **Double-click** vào dòng đã chọn
- Hệ thống sẽ mở dialog "Chi Tiết Hóa Đơn" hiển thị:
  - Danh sách sản phẩm trong đơn hàng
  - Số lượng từng sản phẩm
  - Đơn giá và thành tiền
  - Tổng tiền hóa đơn

### 3. Thông Tin Hiển Thị
Dialog chi tiết hóa đơn sẽ hiển thị:
- **OrderID**: Mã đơn hàng
- **Bảng chi tiết**: Tên sản phẩm, ID sản phẩm, số lượng, đơn giá, thành tiền
- **Tổng tiền hóa đơn**: Tổng số tiền của đơn hàng

## Các Thay Đổi Đã Thực Hiện

### 1. File `TDDonHangJDialog_nghia.java`
- Thêm import cho `HoaDonChiTiet`
- Thêm event listener cho double-click trên cả 2 bảng
- Thêm method `openOrderDetailDialog()` để mở dialog chi tiết
- Thêm tooltip cho các bảng: "Double-click để xem chi tiết hóa đơn"

### 2. Chức Năng Mới
- **Single-click**: Chọn đơn hàng (như cũ)
- **Double-click**: Mở chi tiết hóa đơn (mới)

## Lưu Ý
- Cần chọn đơn hàng trước khi double-click
- Dialog chi tiết sẽ hiển thị modal (không thể tương tác với màn hình chính)
- Có thể đóng dialog bằng nút "Đóng" hoặc nút X

## Troubleshooting
Nếu gặp lỗi khi double-click:
1. Đảm bảo đã chọn đơn hàng trước
2. Kiểm tra console để xem thông báo lỗi
3. Đảm bảo database connection hoạt động
4. Kiểm tra quyền truy cập database

## Tương Thích
- Hoạt động với cả 2 tab: "Lịch Sử Đơn Hàng" và "Đơn Hàng Hiện Tại"
- Tương thích với tất cả trạng thái đơn hàng
- Không ảnh hưởng đến các chức năng hiện có
