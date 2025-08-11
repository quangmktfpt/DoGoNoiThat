-- Script để thêm cột OrderID vào bảng ProductReviews
-- Cho phép đánh giá theo từng đơn hàng riêng biệt

-- Bước 1: Thêm cột OrderID vào bảng ProductReviews
ALTER TABLE ProductReviews 
ADD OrderID INT NULL;

-- Bước 2: Thêm comment để giải thích
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'ID của đơn hàng mà đánh giá này thuộc về', 
    @level0type = N'SCHEMA', @level0name = N'dbo', 
    @level1type = N'TABLE', @level1name = N'ProductReviews', 
    @level2type = N'COLUMN', @level2name = N'OrderID';

-- Bước 3: Thêm foreign key constraint (tùy chọn)
-- ALTER TABLE ProductReviews 
-- ADD CONSTRAINT FK_ProductReviews_Orders 
-- FOREIGN KEY (OrderID) REFERENCES Orders(OrderID);

-- Bước 4: Cập nhật dữ liệu hiện tại (nếu cần)
-- Có thể cập nhật OrderID cho các đánh giá hiện tại dựa trên logic nghiệp vụ
-- Ví dụ: Lấy OrderID đầu tiên mà user đã mua sản phẩm đó

-- Bước 5: Tạo index để tối ưu hiệu suất truy vấn
CREATE INDEX IX_ProductReviews_OrderID ON ProductReviews(OrderID);
CREATE INDEX IX_ProductReviews_ProductID_UserID_OrderID ON ProductReviews(ProductID, UserID, OrderID);

-- Bước 6: Kiểm tra kết quả
SELECT * FROM ProductReviews;
