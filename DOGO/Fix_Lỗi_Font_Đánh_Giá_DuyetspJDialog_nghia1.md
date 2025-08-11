# Fix Lỗi Font Đánh Giá Trong DuyetspJDialog_nghia1

## ✅ Vấn Đề Đã Phát Hiện
- **Text đánh giá hiển thị lỗi font:** `"s?n ph?m ch?a t?t l?"` thay vì `"sản phẩm chưa tốt lắm"`
- **Vấn đề encoding** khi đọc dữ liệu từ database
- **Logic xử lý encoding** không đúng trong method `loadProductReviews`

## Các Thay Đổi Đã Thực Hiện

### 1. **Sửa Logic Xử Lý Encoding Trong DuyetspJDialog_nghia1**
```java
// Sửa lỗi font cho comment - Xử lý encoding đúng cách
String comment = review.getComment() != null ? review.getComment() : "";
System.out.println("DEBUG: Original comment: " + comment);

// Kiểm tra xem comment có ký tự lỗi không
if (comment.contains("?") && comment.length() > 0) {
    try {
        // Thử chuyển đổi từ Windows-1252 sang UTF-8
        byte[] bytes = comment.getBytes("Windows-1252");
        comment = new String(bytes, "UTF-8");
        System.out.println("DEBUG: Fixed comment: " + comment);
    } catch (Exception e) {
        System.err.println("DEBUG: Error fixing encoding: " + e.getMessage());
        // Nếu lỗi, thử cách khác
        try {
            // Thử chuyển đổi từ ISO-8859-1 sang UTF-8
            byte[] bytes = comment.getBytes("ISO-8859-1");
            comment = new String(bytes, "UTF-8");
            System.out.println("DEBUG: Fixed comment (ISO-8859-1): " + comment);
        } catch (Exception e2) {
            System.err.println("DEBUG: Error fixing encoding (ISO-8859-1): " + e2.getMessage());
            // Giữ nguyên text gốc nếu không fix được
            comment = review.getComment() != null ? review.getComment() : "";
        }
    }
}
```

### 2. **Cập Nhật ProductReview Entity**
```java
// Thêm field orderId
private Integer orderId;

// Thêm getter/setter
public Integer getOrderId() {
    return orderId;
}

public void setOrderId(Integer orderId) {
    this.orderId = orderId;
}

// Thêm constructor với orderId
public ProductReview(Integer reviewId, String productId, Integer userId, Byte rating, String comment, LocalDateTime reviewDate, Integer orderId) {
    this.reviewId = reviewId;
    this.productId = productId;
    this.userId = userId;
    this.rating = rating;
    this.comment = comment;
    this.reviewDate = reviewDate;
    this.orderId = orderId;
}
```

### 3. **Cập Nhật ProductReviewDAOImpl**
```java
// Cập nhật method insert để hỗ trợ OrderID
@Override
public void insert(ProductReview review) {
    String sql;
    if (review.getOrderId() != null) {
        sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate, OrderID) VALUES (?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, 
            review.getProductId(),
            review.getUserId(),
            review.getRating(),
            review.getComment(),
            review.getReviewDate(),
            review.getOrderId()
        );
    } else {
        // Fallback cho trường hợp không có OrderID
        sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, 
            review.getProductId(),
            review.getUserId(),
            review.getRating(),
            review.getComment(),
            review.getReviewDate()
        );
    }
}

// Cập nhật method getReviewsByProduct để đọc OrderID
try {
    Integer orderId = rs.getInt("OrderID");
    if (!rs.wasNull()) {
        review.setOrderId(orderId);
    }
} catch (SQLException e) {
    // Cột OrderID có thể chưa tồn tại, bỏ qua
    System.out.println("DEBUG: OrderID column not available in result set");
}
```

## Cách Test

### Bước 1: Test Hiển Thị Đánh Giá Cũ
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Duyệt Sản Phẩm"
3. **Chọn sản phẩm** có đánh giá
4. **Chuyển sang tab** "Đánh giá sản phẩm"
5. **Kiểm tra** text trong cột "Đánh giá"

### Bước 2: Kiểm Tra Console Logs
**Logs mong đợi khi load đánh giá:**
```
DEBUG: Original comment: s?n ph?m ch?a t?t l?
DEBUG: Fixed comment: sản phẩm chưa tốt lắm
```

### Bước 3: Test Đánh Giá Mới
1. **Đánh giá sản phẩm mới** với text tiếng Việt
2. **Kiểm tra** text hiển thị đúng không
3. **Kiểm tra database** để xác nhận dữ liệu được lưu đúng

## Kết Quả Mong Đợi

### ✅ Sau Khi Sửa:
- **Text đánh giá hiển thị đúng** tiếng Việt
- **Không còn ký tự lỗi** như `?`
- **Console logs** hiển thị quá trình fix encoding
- **Dữ liệu được lưu** với encoding đúng

### ❌ Nếu Vẫn Có Lỗi:
- **Console logs** sẽ hiển thị lỗi encoding cụ thể
- **Text gốc** sẽ được giữ nguyên
- **Cần kiểm tra** database collation

## Kiểm Tra Database

### Query Kiểm Tra Encoding:
```sql
SELECT 
    ReviewID,
    ProductID,
    Comment,
    CAST(Comment AS VARBINARY(MAX)) as CommentBytes
FROM ProductReviews 
WHERE ProductID = 'CT401'
ORDER BY ReviewDate DESC;
```

### Kết Quả Mong Đợi:
```
ReviewID | ProductID | Comment | CommentBytes
1        | CT401     | sản phẩm chưa tốt lắm | 0x73E1BAA3...
```

## Lưu Ý Quan Trọng
- **Logic fix encoding** chỉ áp dụng cho text có ký tự lỗi
- **Debug logs** giúp theo dõi quá trình xử lý
- **Fallback mechanism** đảm bảo không bị crash
- **OrderID** được hỗ trợ đầy đủ

## Bước Tiếp Theo
1. **Test hiển thị đánh giá cũ** để xác nhận font đúng
2. **Test đánh giá mới** với text tiếng Việt
3. **Kiểm tra console logs** để theo dõi quá trình fix
4. **Kiểm tra database** để xác nhận dữ liệu

**Chạy test và cho tôi biết text đánh giá có hiển thị đúng tiếng Việt không!**
