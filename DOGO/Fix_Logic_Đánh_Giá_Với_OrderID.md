# Fix Logic Đánh Giá Với OrderID

## ✅ Vấn Đề Đã Phát Hiện
- **Bảng ProductReviews thiếu cột OrderID**
- **Logic hiện tại** chỉ kiểm tra ProductID + UserID
- **Cần hỗ trợ** đánh giá theo từng đơn hàng riêng biệt
- **Một user có thể mua cùng sản phẩm** trong nhiều đơn hàng khác nhau

## Phân Tích Vấn Đề

### Bảng ProductReviews Hiện Tại:
```
ReviewID | ProductID | UserID | Rating | Comment | ReviewDate
1        | PROD001   | 2      | 5      | "Hàng tốt" | 2025-07-07
2        | PROD002   | 3      | 4      | "không gian nhà..." | 2025-07-07
3        | G01       | 4      | 2      | "form ngu kkkkkkkkkkkkk" | 2025-07-28
4        | BP212     | 4      | 5      | "sản phẩm tốt" | 2025-08-11
```

### Vấn Đề:
- **Thiếu cột OrderID** để xác định đánh giá thuộc về đơn hàng nào
- **User có thể mua cùng sản phẩm** trong nhiều đơn hàng
- **Cần đánh giá riêng** cho từng đơn hàng

## Các Thay Đổi Đã Thực Hiện

### 1. **Script SQL Thêm Cột OrderID**
```sql
-- Thêm cột OrderID vào bảng ProductReviews
ALTER TABLE ProductReviews 
ADD OrderID INT NULL;

-- Tạo index để tối ưu hiệu suất
CREATE INDEX IX_ProductReviews_OrderID ON ProductReviews(OrderID);
CREATE INDEX IX_ProductReviews_ProductID_UserID_OrderID ON ProductReviews(ProductID, UserID, OrderID);
```

### 2. **Cập Nhật ProductReviewDAO**
```java
// Thêm method mới
boolean hasUserReviewedInOrder(String productId, Integer userId, Integer orderId);
```

### 3. **Cập Nhật ProductReviewDAOImpl**
```java
@Override
public boolean hasUserReviewedInOrder(String productId, Integer userId, Integer orderId) {
    String sql = "SELECT COUNT(*) as count FROM ProductReviews WHERE ProductID = ? AND UserID = ? AND OrderID = ?";
    try {
        ResultSet rs = XJdbc.executeQuery(sql, productId, userId, orderId);
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
    } catch (SQLException e) {
        // Fallback nếu cột OrderID chưa tồn tại
        System.out.println("DEBUG: OrderID column not found, falling back to old method");
        return hasUserReviewed(productId, userId);
    }
    return false;
}
```

### 4. **Cập Nhật TDDonHangJDialog_nghia**
```java
// Kiểm tra đánh giá theo đơn hàng cụ thể
boolean hasReviewed = productReviewDAO.hasUserReviewedInOrder(productId, currentUserId, currentOrder.getOrderId());

// Lọc sản phẩm chưa đánh giá trong đơn hàng
boolean hasReviewed = productReviewDAO.hasUserReviewedInOrder(productId, currentUserId, currentOrder.getOrderId());
```

### 5. **Cập Nhật DanhGiaJDialog1**
```java
// Thêm field orderId
private Integer orderId;

// Lưu đánh giá với OrderID
if (orderId != null) {
    sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate, OrderID) VALUES (?, ?, ?, ?, GETDATE(), ?)";
    poly.util.XJdbc.executeUpdate(sql, productId, userId, Integer.parseInt(soSao), binhLuan, orderId);
} else {
    // Fallback cho trường hợp không có OrderID
    sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, GETDATE())";
    poly.util.XJdbc.executeUpdate(sql, productId, userId, Integer.parseInt(soSao), binhLuan);
}
```

## Cách Triển Khai

### Bước 1: Chạy Script SQL
1. **Mở SQL Server Management Studio**
2. **Chạy script** `add_orderid_to_productreviews.sql`
3. **Kiểm tra** bảng ProductReviews có cột OrderID

### Bước 2: Test Logic Mới
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Theo Dõi Đơn Hàng"
3. **Chọn đơn hàng** có trạng thái "Completed"
4. **Click button "Đánh giá"**
5. **Đánh giá sản phẩm** và gửi

### Bước 3: Test Đánh Giá Theo Đơn Hàng
1. **Tạo đơn hàng mới** với cùng sản phẩm
2. **Hoàn thành đơn hàng** (Completed)
3. **Đánh giá lại** sản phẩm trong đơn hàng mới
4. **Kiểm tra** có thể đánh giá được không

## Kết Quả Mong Đợi

### ✅ Sau Khi Thêm OrderID:
- **Mỗi đơn hàng** có thể đánh giá riêng biệt
- **User có thể đánh giá** cùng sản phẩm trong nhiều đơn hàng
- **Logic kiểm tra** chính xác theo ProductID + UserID + OrderID
- **Dữ liệu đánh giá** được lưu với OrderID

### ❌ Nếu Chưa Thêm OrderID:
- **Fallback về logic cũ** (ProductID + UserID)
- **Thông báo debug** "OrderID column not found"
- **Vẫn hoạt động** nhưng không phân biệt đơn hàng

## Console Logs Mong Đợi

### Khi Có Cột OrderID:
```
DEBUG: User 1 has reviewed product BP212 in order 28: false
DEBUG: User 1 has reviewed product BP212 in order 29: true
```

### Khi Chưa Có Cột OrderID:
```
DEBUG: OrderID column not found, falling back to old method
DEBUG: User 1 has reviewed product BP212: true
```

## Lưu Ý Quan Trọng
- **Chạy script SQL trước** khi test logic mới
- **Fallback mechanism** đảm bảo hoạt động ngay cả khi chưa có OrderID
- **Dữ liệu cũ** sẽ có OrderID = NULL
- **Có thể cập nhật** OrderID cho dữ liệu cũ sau này

## Bước Tiếp Theo
1. **Chạy script SQL** để thêm cột OrderID
2. **Test logic mới** với nhiều đơn hàng
3. **Kiểm tra** có thể đánh giá riêng từng đơn hàng không
4. **Cập nhật dữ liệu cũ** nếu cần

**Chạy script SQL và test logic mới để xác nhận hoạt động đúng!**
