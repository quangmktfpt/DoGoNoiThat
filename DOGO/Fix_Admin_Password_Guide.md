# Hướng dẫn Fix Password cho User Admin

## Vấn đề hiện tại:
Password của user admin trong database chưa được hash. Thay vì lưu hash, nó đang lưu trực tiếp "123456".

## Cách khắc phục:

### Bước 1: Chạy script để hash password
1. Mở project trong IDE
2. Tìm file `src/main/java/poly/util/FixAdminPassword.java`
3. Chạy file này (Run as Java Application)

### Bước 2: Kiểm tra kết quả
Script sẽ in ra:
```
Tìm thấy user admin:
Username: admin
Password hiện tại: 123456
Đã hash password thành công!
Password hash mới: [salt]:[hash]
✅ Đăng nhập thành công!
```

### Bước 3: Test đăng nhập
1. Chạy ứng dụng
2. Đăng nhập với:
   - Username: admin
   - Password: 123456
3. Kiểm tra xem có đăng nhập thành công không

## Nếu muốn hash tất cả password trong database:

### Chạy script hash tất cả:
1. Tìm file `src/main/java/poly/util/PasswordHashFixer.java`
2. Chạy file này để hash tất cả password chưa được hash

## Nếu gặp lỗi:

### Lỗi 1: Không tìm thấy user admin
```sql
-- Kiểm tra user admin có tồn tại không
SELECT * FROM Users WHERE Username = 'admin';
```

### Lỗi 2: Không thể update
```sql
-- Kiểm tra quyền database
-- Đảm bảo có quyền UPDATE trên bảng Users
```

### Lỗi 3: Vẫn không đăng nhập được
1. Kiểm tra console output khi đăng nhập
2. Đảm bảo password đã được hash đúng format
3. Test lại với script `FixAdminPassword`

## Kiểm tra password đã được hash:

### Trước khi fix:
```sql
SELECT Username, PasswordHash FROM Users WHERE Username = 'admin';
-- Kết quả: admin | 123456
```

### Sau khi fix:
```sql
SELECT Username, PasswordHash FROM Users WHERE Username = 'admin';
-- Kết quả: admin | [salt]:[hash]
```

## Lưu ý quan trọng:
1. **Backup database** trước khi chạy script
2. **Chỉ chạy script một lần** để tránh hash lại password đã hash
3. **Test đăng nhập** sau khi chạy script
4. **Xóa debug code** trong `DNhapJDialog.java` sau khi fix xong

## Trạng thái sau khi fix:
✅ Password được hash đúng cách
✅ Đăng nhập hoạt động bình thường
✅ Bảo mật được cải thiện 