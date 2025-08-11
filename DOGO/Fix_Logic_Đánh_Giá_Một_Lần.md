# Fix Logic Đánh Giá Một Lần Cho Mỗi Sản Phẩm

## ✅ Vấn Đề Đã Khắc Phục
- **Một người chỉ có thể đánh giá một lần** cho mỗi sản phẩm
- **Chỉ hiển thị sản phẩm chưa đánh giá** trong combobox
- **Thông báo lỗi** khi cố gắng đánh giá lại
- **Kiểm tra trạng thái đơn hàng** phải là "Completed"

## Các Thay Đổi Đã Thực Hiện

### 1. **Thêm ProductReviewDAO**
```java
import poly.dao.impl.ProductReviewDAOImpl;
private ProductReviewDAOImpl productReviewDAO = new ProductReviewDAOImpl();
```

### 2. **Kiểm Tra Đánh Giá Trước Khi Mở Dialog**
```java
// Kiểm tra xem user đã đánh giá sản phẩm này chưa
boolean hasReviewed = productReviewDAO.hasUserReviewed(productId, currentUserId);
System.out.println("DEBUG: User " + currentUserId + " has reviewed product " + productId + ": " + hasReviewed);

if (hasReviewed) {
    XDialog.alert("Bạn đã đánh giá sản phẩm này rồi! Mỗi sản phẩm chỉ có thể đánh giá một lần.");
    return;
}
```

### 3. **Lọc Sản Phẩm Chưa Đánh Giá Trong Combobox**
```java
// Tạo combobox chọn sản phẩm (chỉ những sản phẩm chưa được đánh giá)
java.util.List<String> availableProducts = new java.util.ArrayList<>();

for (OrderDetail detail : orderDetails) {
    String productId = detail.getProductId().trim();
    
    // Kiểm tra xem user đã đánh giá sản phẩm này chưa
    boolean hasReviewed = productReviewDAO.hasUserReviewed(productId, currentUserId);
    
    if (!hasReviewed) {
        // Chỉ thêm sản phẩm chưa đánh giá vào combobox
        poly.entity.Product product = productDAO.selectById(productId);
        String productName = product != null ? product.getProductName() : productId;
        String option = productName + " (ID: " + productId + ")";
        availableProducts.add(option);
    }
}

if (availableProducts.isEmpty()) {
    XDialog.alert("Bạn đã đánh giá tất cả sản phẩm trong đơn hàng này rồi!");
    return;
}
```

## Cách Test

### Bước 1: Test Đánh Giá Lần Đầu
1. **Chạy ứng dụng** và đăng nhập
2. **Mở màn hình** "Theo Dõi Đơn Hàng"
3. **Chọn đơn hàng** có trạng thái "Completed"
4. **Click button "Đánh giá"**
5. **Chọn sản phẩm** từ combobox
6. **Đánh giá sản phẩm** và gửi

### Bước 2: Test Đánh Giá Lần Thứ Hai
1. **Quay lại** màn hình "Theo Dõi Đơn Hàng"
2. **Chọn cùng đơn hàng** đã đánh giá
3. **Click button "Đánh giá"**
4. **Kiểm tra** sản phẩm đã đánh giá không còn trong combobox

### Bước 3: Test Đánh Giá Sản Phẩm Đã Đánh Giá
1. **Nếu cố gắng** chọn sản phẩm đã đánh giá
2. **Sẽ hiển thị thông báo:** "Bạn đã đánh giá sản phẩm này rồi! Mỗi sản phẩm chỉ có thể đánh giá một lần."

## Kết Quả Mong Đợi

### ✅ Khi Đánh Giá Lần Đầu:
- **Combobox hiển thị** tất cả sản phẩm trong đơn hàng
- **Có thể chọn** và đánh giá sản phẩm
- **Đánh giá được lưu** vào database

### ✅ Khi Đánh Giá Lần Thứ Hai:
- **Combobox chỉ hiển thị** sản phẩm chưa đánh giá
- **Sản phẩm đã đánh giá** không còn trong danh sách
- **Nếu tất cả đã đánh giá:** Hiển thị "Bạn đã đánh giá tất cả sản phẩm trong đơn hàng này rồi!"

### ❌ Khi Cố Gắng Đánh Giá Lại:
- **Thông báo lỗi:** "Bạn đã đánh giá sản phẩm này rồi! Mỗi sản phẩm chỉ có thể đánh giá một lần."
- **Dialog không mở**

## Console Logs Mong Đợi

### Khi Kiểm Tra Sản Phẩm:
```
DEBUG: Checking product BP212 for user 1
DEBUG: User has reviewed BP212: false
DEBUG: Added to available products: Bàn phấn gỗ đá cẩm thạch (ID: BP212)
```

### Khi Sản Phẩm Đã Đánh Giá:
```
DEBUG: Checking product BP212 for user 1
DEBUG: User has reviewed BP212: true
DEBUG: Skipped already reviewed product: BP212
```

### Khi Kiểm Tra Trước Khi Mở Dialog:
```
DEBUG: User 1 has reviewed product BP212: true
```

## Lưu Ý Quan Trọng
- **Chỉ đơn hàng "Completed"** mới có thể đánh giá
- **Mỗi sản phẩm** chỉ được đánh giá một lần
- **Combobox tự động lọc** sản phẩm chưa đánh giá
- **Thông báo rõ ràng** khi không thể đánh giá

## Bước Tiếp Theo
1. **Test đánh giá lần đầu** cho một sản phẩm
2. **Test đánh giá lần thứ hai** cho cùng sản phẩm
3. **Kiểm tra combobox** chỉ hiển thị sản phẩm chưa đánh giá
4. **Test với nhiều sản phẩm** trong cùng đơn hàng

**Chạy test và cho tôi biết logic mới có hoạt động đúng không!**
