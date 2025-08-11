# Tính Năng Đánh Giá Sản Phẩm - Hướng Dẫn Test

## ✅ Tính Năng Đã Implement

### 1. **Button "Đánh giá" trong TDDonHangJDialog_nghia**
- Chỉ hiển thị cho đơn hàng có trạng thái "Completed"
- Hiển thị combobox chọn sản phẩm để đánh giá
- Mở dialog đánh giá với thông tin đầy đủ

### 2. **Dialog Đánh Giá (DanhGiaJDialog1)**
- Hiển thị hình ảnh sản phẩm trong `lblHinhAnh`
- Hiển thị tên sản phẩm và ID đơn hàng trong `jLabel1`
- Hệ thống đánh giá 5 sao
- Text area để nhập nhận xét

## Các Thay Đổi Đã Thực Hiện

### 1. **TDDonHangJDialog_nghia.java**
```java
// Thêm event handler cho jButton1
jButton1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
    }
});

// Method mở dialog đánh giá
private void openRatingDialog() {
    // Kiểm tra đơn hàng được chọn
    // Kiểm tra trạng thái "Completed"
    // Hiển thị combobox chọn sản phẩm
    // Mở DanhGiaJDialog1 với thông tin đầy đủ
}
```

### 2. **DanhGiaJDialog1.java**
```java
// Constructor mới với orderId
public DanhGiaJDialog1(Frame parent, String productId, int userId, 
    String tenSanPham, ImageIcon hinhAnh, boolean daMua, Integer orderId)

// Method setupComponents mới
private void setupComponents(String tenSanPham, ImageIcon hinhAnh, Integer orderId) {
    // Hiển thị: "Bạn đang đánh giá sản phẩm X (Đơn hàng #Y)"
    // Hiển thị hình ảnh sản phẩm
}
```

## Cách Test

### Bước 1: Chuẩn Bị
1. **Chạy ứng dụng** và đăng nhập
2. **Đảm bảo có đơn hàng** với trạng thái "Completed"
3. **Mở màn hình** "Theo Dõi Đơn Hàng"

### Bước 2: Test Button Đánh Giá
1. **Chọn đơn hàng** trong tab "Lịch Sử Đơn Hàng"
2. **Kiểm tra trạng thái** - phải là "Completed"
3. **Click button "Đánh giá"**

### Bước 3: Test Combobox Chọn Sản Phẩm
1. **Dialog chọn sản phẩm** sẽ hiện ra
2. **Combobox hiển thị** danh sách sản phẩm trong đơn hàng
3. **Format:** "Tên Sản Phẩm (ID: ProductID)"
4. **Chọn một sản phẩm** và click OK

### Bước 4: Test Dialog Đánh Giá
1. **Dialog đánh giá mở** với thông tin:
   - **Tiêu đề:** "Đánh giá sản phẩm X (Đơn hàng #Y)"
   - **jLabel1:** "Bạn đang đánh giá sản phẩm X (Đơn hàng #Y)"
   - **lblHinhAnh:** Hiển thị hình ảnh sản phẩm
2. **Chọn số sao** (1-5)
3. **Nhập nhận xét** trong text area
4. **Click "Gửi"**

### Bước 5: Kiểm Tra Kết Quả
1. **Thông báo:** "Cảm ơn bạn đã đánh giá sản phẩm!"
2. **Dialog đóng** và quay về màn hình chính

## Kết Quả Mong Đợi

### ✅ Button "Đánh giá":
- **Chỉ hiển thị** cho đơn hàng "Completed"
- **Hiển thị combobox** chọn sản phẩm
- **Thông báo lỗi** nếu chưa chọn đơn hàng

### ✅ Dialog Đánh Giá:
- **Hình ảnh sản phẩm** hiển thị đúng
- **Tên sản phẩm + ID đơn hàng** hiển thị rõ ràng
- **Hệ thống 5 sao** hoạt động
- **Text area** cho phép nhập nhận xét

### ✅ Validation:
- **Chỉ đơn hàng "Completed"** mới đánh giá được
- **Phải chọn đơn hàng** trước khi đánh giá
- **Phải chọn sản phẩm** trong combobox

## Troubleshooting

### Nếu Button Không Hiện:
1. **Kiểm tra trạng thái** đơn hàng - phải là "Completed"
2. **Kiểm tra** đã chọn đơn hàng chưa

### Nếu Combobox Trống:
1. **Kiểm tra** đơn hàng có sản phẩm không
2. **Kiểm tra** database connection

### Nếu Hình Ảnh Không Hiện:
1. **Kiểm tra** file hình ảnh trong thư mục `product_images/`
2. **Kiểm tra** `imagePath` trong database

### Nếu Dialog Không Mở:
1. **Kiểm tra import** `DanhGiaJDialog1`
2. **Kiểm tra constructor** có đúng tham số không

## Lưu Ý Quan Trọng
- **Chỉ đơn hàng "Completed"** mới đánh giá được
- **Phải chọn đơn hàng** trước khi click "Đánh giá"
- **Hình ảnh sản phẩm** sẽ resize về 150x150px
- **Tiêu đề dialog** hiển thị đầy đủ thông tin

**Test và cho tôi biết kết quả!**
