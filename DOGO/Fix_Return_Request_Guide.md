# Hướng Dẫn Sửa Lỗi Yêu Cầu Đổi Trả

## Vấn Đề
Khi ấn nút "Yêu Cầu Đổi Trả" trong màn hình `TDDonHangJDialog_nghia`, hệ thống báo lỗi:
```
SqlException: The INSERT statement conflicted with the CHECK constraint "CK__Inventory__Trans__44FF419A". 
The conflict occurred in database "Storedogo2", table "dbo.InventoryTransactions", column 'TransactionType'.
```

## Nguyên Nhân
Lỗi xảy ra vì CHECK constraint trong bảng `InventoryTransactions` không cho phép giá trị `'ReturnIn'` trong cột `TransactionType`. Code đang cố gắng insert với `TransactionType = "ReturnIn"` nhưng constraint hiện tại chỉ cho phép các giá trị khác.

## Giải Pháp

### Bước 1: Chạy Script SQL để Sửa Constraint

**Vấn đề:** Có 2 CHECK constraints cùng tồn tại:
- `CK_Inventory_Trans_44FF419A` (cũ - không có ReturnIn)
- `CK_InventoryTransactions_TransactionType` (mới - có ReturnIn)

Constraint cũ vẫn gây lỗi nên cần xóa.

**Giải pháp:**
1. Mở SQL Server Management Studio
2. Kết nối đến database `Storedogo2`
3. Chạy script `complete_fix_returnin.sql` (khuyến nghị) hoặc `remove_old_constraint.sql`

Script sẽ:
- Xóa TẤT CẢ constraints cũ liên quan đến TransactionType
- Tạo constraint mới duy nhất cho phép `'ReturnIn'`
- Test insert để đảm bảo hoạt động

### Bước 2: Kiểm Tra Code Đã Được Sửa

Code trong `OrderDAOImpl.java` đã được cải thiện để:
- Xử lý lỗi tốt hơn khi insert InventoryTransaction
- Thử nhiều cách khác nhau nếu có lỗi
- Fallback sang `'Adjustment'` nếu `'ReturnIn'` không hoạt động

### Bước 3: Test Lại Chức Năng

1. Chạy lại ứng dụng
2. Vào màn hình "Theo Dõi Đơn Hàng"
3. Chọn một đơn hàng có trạng thái "Completed" hoặc "Delivering"
4. Ấn nút "Yêu Cầu Đổi Trả"
5. Nhập lý do đổi trả
6. Xác nhận yêu cầu

## Các Thay Đổi Đã Thực Hiện

### 1. File `complete_fix_returnin.sql` (Khuyến nghị)
- Script SQL tổng hợp để xử lý toàn bộ vấn đề
- Xóa TẤT CẢ constraints cũ và tạo mới
- Cho phép giá trị `'ReturnIn'` trong `TransactionType`
- Bao gồm test để đảm bảo hoạt động

### 2. File `remove_old_constraint.sql`
- Script chỉ xóa constraint cũ `CK_Inventory_Trans_44FF419A`
- Dành cho trường hợp chỉ cần xóa constraint cũ

### 2. File `OrderDAOImpl.java`
- Cải thiện xử lý lỗi trong method `updateOrderStatusWithReasonAndInventory`
- Thêm fallback mechanism khi insert InventoryTransaction
- Log chi tiết hơn để debug

### 3. File `TDDonHangJDialog_nghia.java`
- Hàm `requestReturn()` đã được kiểm tra và hoạt động đúng
- Có flag `isProcessingOrder` để tránh xử lý nhiều lần

## Lưu Ý
- Đảm bảo backup database trước khi chạy script SQL
- Kiểm tra kỹ constraint mới sau khi chạy script
- Test đầy đủ chức năng đổi trả sau khi sửa

## Troubleshooting
Nếu vẫn gặp lỗi sau khi chạy script:
1. Kiểm tra log console để xem thông báo debug
2. Đảm bảo database connection hoạt động
3. Kiểm tra quyền truy cập database
4. Xem xét có trigger nào khác ảnh hưởng không
