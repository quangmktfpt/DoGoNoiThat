-- Script thêm cột CouponID vào bảng Addresses
USE Storedogo2;

-- Thêm cột CouponID vào bảng Addresses
ALTER TABLE Addresses 
ADD CouponID VARCHAR(20) NULL;

-- Thêm foreign key constraint (tùy chọn)
-- ALTER TABLE Addresses 
-- ADD CONSTRAINT FK_Addresses_Coupons 
-- FOREIGN KEY (CouponID) REFERENCES Coupons(CouponID);

-- Kiểm tra kết quả
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' 
  AND COLUMN_NAME = 'CouponID';

PRINT 'Đã thêm cột CouponID vào bảng Addresses thành công!';
PRINT 'Bây giờ có thể đặt hàng với mã giảm giá mà không bị lỗi.';
