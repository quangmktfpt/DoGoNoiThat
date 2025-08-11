# Test Logic Đánh Giá Với OrderID Đã Sửa

## ✅ Logic Đã Được Cập Nhật
- **Bảng ProductReviews** đã có cột OrderID
- **Logic insert** luôn sử dụng OrderID
- **Logic kiểm tra** đánh giá theo ProductID + UserID + OrderID
- **Debug logs** chi tiết để theo dõi quá trình

## Các Thay Đổi Đã Thực Hiện

### 1. **Cập Nhật DanhGiaJDialog1 - Logic Insert**
```java
// Debug logs chi tiết
System.out.println("DEBUG: Saving review to database:");
System.out.println("DEBUG: - ProductID: " + productId);
System.out.println("DEBUG: - UserID: " + userId);
System.out.println("DEBUG: - OrderID: " + orderId);
System.out.println("DEBUG: - Rating: " + soSao);
System.out.println("DEBUG: - Comment: " + binhLuan);

// Luôn sử dụng OrderID nếu có
sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate, OrderID) VALUES (?, ?, ?, ?, GETDATE(), ?)";
poly.util.XJdbc.executeUpdate(sql, productId, userId, Integer.parseInt(soSao), binhLuan, orderId);
```

### 2. **Cập Nhật ProductReviewDAOImpl - Logic Kiểm Tra**
```java
@Override
public boolean hasUserReviewedInOrder(String productId, Integer userId, Integer orderId) {
    String sql = "SELECT COUNT(*) as count FROM ProductReviews WHERE ProductID = ? AND UserID = ? AND OrderID = ?";
    try {
        System.out.println("DEBUG: Checking review with OrderID - ProductID: " + productId + ", UserID: " + userId + ", OrderID: " + orderId);
        ResultSet rs = XJdbc.executeQuery(sql, productId, userId, orderId);
        if (rs.next()) {
            int count = rs.getInt("count");
            System.out.println("DEBUG: Found " + count + " reviews for this product/user/order combination");
            return count > 0;
        }
    } catch (SQLException e) {
        System.err.println("DEBUG: Error checking review with OrderID: " + e.getMessage());
        return hasUserReviewed(productId, userId); // Fallback
    }
    return false;
}
```

## Cách Test

### Bước 1: Test Đánh Giá Lần Đầu
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Theo Dõi Đơn Hàng"
3. **Chọn đơn hàng** có trạng thái "Completed"
4. **Click button "Đánh giá"**
5. **Chọn sản phẩm** từ combobox
6. **Đánh giá sản phẩm** (chọn sao và viết comment)
7. **Click "Gửi"**

### Bước 2: Kiểm Tra Console Logs
**Logs mong đợi khi đánh giá:**
```
DEBUG: User 1 has reviewed product BP212 in order 28: false
DEBUG: Setting up image in DanhGiaJDialog1
DEBUG: hinhAnh parameter: Not NULL
DEBUG: Setting product image to lblHinhAnh
DEBUG: Image size: 256x256

DEBUG: Saving review to database:
DEBUG: - ProductID: BP212
DEBUG: - UserID: 1
DEBUG: - OrderID: 28
DEBUG: - Rating: 5
DEBUG: - Comment: Sản phẩm rất tốt!
DEBUG: Using SQL with OrderID: INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate, OrderID) VALUES (?, ?, ?, ?, GETDATE(), ?)
DEBUG: Review saved successfully with OrderID
```

### Bước 3: Test Đánh Giá Lần Thứ Hai (Cùng Đơn Hàng)
1. **Quay lại** màn hình "Theo Dõi Đơn Hàng"
2. **Chọn cùng đơn hàng** đã đánh giá
3. **Click button "Đánh giá"**
4. **Kiểm tra** sản phẩm đã đánh giá không còn trong combobox

**Logs mong đợi:**
```
DEBUG: User 1 has reviewed product BP212 in order 28: true
DEBUG: Skipped already reviewed product: BP212
```

### Bước 4: Test Đánh Giá Đơn Hàng Khác (Cùng Sản Phẩm)
1. **Tạo đơn hàng mới** với cùng sản phẩm
2. **Hoàn thành đơn hàng** (Completed)
3. **Đánh giá lại** sản phẩm trong đơn hàng mới

**Logs mong đợi:**
```
DEBUG: User 1 has reviewed product BP212 in order 29: false
DEBUG: Added to available products: Bàn phấn gỗ đá cẩm thạch (ID: BP212)
```

## Kết Quả Mong Đợi

### ✅ Khi Đánh Giá Lần Đầu:
- **Combobox hiển thị** tất cả sản phẩm trong đơn hàng
- **Có thể chọn** và đánh giá sản phẩm
- **Đánh giá được lưu** với OrderID chính xác
- **Console logs** hiển thị thông tin chi tiết

### ✅ Khi Đánh Giá Lần Thứ Hai (Cùng Đơn Hàng):
- **Combobox chỉ hiển thị** sản phẩm chưa đánh giá
- **Sản phẩm đã đánh giá** không còn trong danh sách
- **Thông báo** nếu tất cả đã đánh giá

### ✅ Khi Đánh Giá Đơn Hàng Khác (Cùng Sản Phẩm):
- **Có thể đánh giá** cùng sản phẩm trong đơn hàng mới
- **Logic phân biệt** theo OrderID
- **Dữ liệu được lưu** riêng biệt cho từng đơn hàng

## Kiểm Tra Database

### Query Kiểm Tra Đánh Giá:
```sql
SELECT 
    ReviewID,
    ProductID,
    UserID,
    OrderID,
    Rating,
    Comment,
    ReviewDate
FROM ProductReviews 
WHERE ProductID = 'BP212' AND UserID = 1
ORDER BY ReviewDate DESC;
```

### Kết Quả Mong Đợi:
```
ReviewID | ProductID | UserID | OrderID | Rating | Comment | ReviewDate
1        | BP212     | 1      | 28      | 5      | "Sản phẩm rất tốt!" | 2025-01-XX
2        | BP212     | 1      | 29      | 4      | "Tốt nhưng hơi nhỏ" | 2025-01-XX
```

## Lưu Ý Quan Trọng
- **OrderID luôn được sử dụng** khi insert đánh giá
- **Logic kiểm tra** chính xác theo ProductID + UserID + OrderID
- **Debug logs** giúp theo dõi quá trình
- **Fallback mechanism** vẫn hoạt động nếu có lỗi

## Bước Tiếp Theo
1. **Test đánh giá lần đầu** cho một sản phẩm
2. **Kiểm tra console logs** để xác nhận OrderID được sử dụng
3. **Test đánh giá lần thứ hai** cho cùng đơn hàng
4. **Test đánh giá** cho đơn hàng khác với cùng sản phẩm
5. **Kiểm tra database** để xác nhận dữ liệu được lưu đúng

**Chạy test và cho tôi biết console logs hiển thị gì!**
