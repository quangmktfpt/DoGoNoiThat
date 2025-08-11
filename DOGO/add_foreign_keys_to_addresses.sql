-- Script thêm foreign key constraints vào bảng Addresses
USE Storedogo2;

-- Thêm foreign key constraint cho UserID (nếu chưa có)
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
               WHERE CONSTRAINT_NAME = 'FK_Addresses_Users' AND TABLE_NAME = 'Addresses')
BEGIN
    ALTER TABLE Addresses 
    ADD CONSTRAINT FK_Addresses_Users 
    FOREIGN KEY (UserID) REFERENCES Users(UserID);
    PRINT 'Đã thêm FK_Addresses_Users thành công!';
END
ELSE
BEGIN
    PRINT 'FK_Addresses_Users đã tồn tại!';
END

-- Thêm foreign key constraint cho CouponID
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
               WHERE CONSTRAINT_NAME = 'FK_Addresses_Coupons' AND TABLE_NAME = 'Addresses')
BEGIN
    ALTER TABLE Addresses 
    ADD CONSTRAINT FK_Addresses_Coupons 
    FOREIGN KEY (CouponID) REFERENCES Coupons(CouponID);
    PRINT 'Đã thêm FK_Addresses_Coupons thành công!';
END
ELSE
BEGIN
    PRINT 'FK_Addresses_Coupons đã tồn tại!';
END

-- Kiểm tra kết quả
SELECT 
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE,
    TABLE_NAME
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_NAME = 'Addresses' 
  AND CONSTRAINT_TYPE = 'FOREIGN KEY'
ORDER BY CONSTRAINT_NAME;

PRINT 'Hoàn thành! Đã kiểm tra tất cả foreign key constraints của bảng Addresses.';
