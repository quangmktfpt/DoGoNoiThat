# Debug Vấn Đề Không Mở Được DanhGiaJDialog1

## ✅ Vấn Đề Đã Khắc Phục
- **Thiếu import** `DanhGiaJDialog1`
- **Thêm debug logs** để xác định nguyên nhân lỗi
- **Cải thiện thông báo lỗi** với ProductID

## Các Thay Đổi Đã Thực Hiện

### 1. **Thêm Import**
```java
import poly.ui.DanhGiaJDialog1;
```

### 2. **Thêm Debug Logs**
```java
System.out.println("DEBUG: Selected product string: " + selectedProduct);
System.out.println("DEBUG: Extracted productId: " + productId);
System.out.println("DEBUG: Product from database: " + (product != null ? product.getProductName() : "NULL"));

if (product == null) {
    System.err.println("DEBUG: Product not found for ID: " + productId);
    XDialog.alert("Không tìm thấy thông tin sản phẩm! ProductID: " + productId);
    return;
}
```

## Cách Debug

### Bước 1: Chạy Ứng Dụng
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Theo Dõi Đơn Hàng"
3. **Chọn đơn hàng** có trạng thái "Completed"

### Bước 2: Test Button Đánh Giá
1. **Click button "Đánh giá"**
2. **Chọn sản phẩm** từ combobox
3. **Kiểm tra console logs** để xem debug information

### Bước 3: Kiểm Tra Console Logs
**Logs mong đợi:**
```
DEBUG: Selected product string: Tên Sản Phẩm (ID: ProductID)
DEBUG: Extracted productId: ProductID
DEBUG: Product from database: Tên Sản Phẩm
```

**Nếu có lỗi:**
```
DEBUG: Selected product string: Tên Sản Phẩm (ID: ProductID)
DEBUG: Extracted productId: ProductID
DEBUG: Product from database: NULL
DEBUG: Product not found for ID: ProductID
```

## Nguyên Nhân Có Thể

### 1. **ProductID Không Khớp**
- ProductID trong OrderDetails không tồn tại trong bảng Products
- Kiểm tra database để xác nhận

### 2. **Database Connection**
- Kết nối database có vấn đề
- ProductDAO không hoạt động đúng

### 3. **Method selectById**
- Method `selectById` trong ProductDAO có lỗi
- Cần kiểm tra implementation

## Cách Kiểm Tra Database

### Query 1: Kiểm tra Products
```sql
SELECT ProductID, ProductName FROM Products;
```

### Query 2: Kiểm tra OrderDetails
```sql
SELECT od.OrderID, od.ProductID, od.Quantity, p.ProductName
FROM OrderDetails od
LEFT JOIN Products p ON od.ProductID = p.ProductID
WHERE od.OrderID = [OrderID];
```

### Query 3: Tìm ProductID cụ thể
```sql
SELECT * FROM Products WHERE ProductID = '[ProductID từ logs]';
```

## Kết Quả Mong Đợi

### ✅ Nếu Hoạt Động Đúng:
1. **Console logs** hiển thị thông tin đầy đủ
2. **Dialog DanhGiaJDialog1** mở
3. **Thông tin sản phẩm** hiển thị đúng

### ❌ Nếu Có Lỗi:
1. **Console logs** hiển thị ProductID không tìm thấy
2. **Thông báo lỗi** với ProductID cụ thể
3. **Dialog không mở**

## Troubleshooting

### Nếu ProductID Không Tìm Thấy:
1. **Kiểm tra bảng Products** có dữ liệu không
2. **So sánh ProductID** giữa OrderDetails và Products
3. **Thêm dữ liệu** vào bảng Products nếu thiếu

### Nếu Database Connection Lỗi:
1. **Kiểm tra connection string**
2. **Kiểm tra database server**
3. **Test query trực tiếp**

### Nếu Method selectById Lỗi:
1. **Kiểm tra ProductDAOImpl**
2. **Kiểm tra SQL query**
3. **Test method riêng lẻ**

## Lưu Ý Quan Trọng
- **Debug logs** sẽ hiển thị trong console
- **Thông báo lỗi** sẽ hiển thị ProductID cụ thể
- **Import DanhGiaJDialog1** đã được thêm
- **Fallback mechanism** vẫn hoạt động

## Bước Tiếp Theo
1. **Chạy test** và xem console logs
2. **Xác định nguyên nhân** từ logs
3. **Sửa lỗi** tương ứng
4. **Test lại** để xác nhận

**Chạy test và cho tôi biết console logs hiển thị gì!**
