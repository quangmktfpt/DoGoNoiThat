# Hướng Dẫn Sử Dụng Các Trường Unique (Bắt Buộc)

## Tổng Quan

Trong dự án này, **Unique** có nghĩa là các trường **bắt buộc phải nhập** (mandatory fields) - những trường mà người dùng không được phép để trống khi thực hiện các thao tác như đăng ký, tạo mới, cập nhật dữ liệu.

## Các Trường Unique Trong Dự Án

### 1. Form Đăng Ký Tài Khoản (`DKyTaiKhoanJDialog`)

**Các trường bắt buộc:**
- **Họ Tên** (`jTextField1`) - Ít nhất 2 ký tự
- **Tên Đăng Nhập** (`jTextField2`) - Ít nhất 3 ký tự, chỉ chứa chữ cái, số và dấu gạch dưới
- **Mật Khẩu** (`jPasswordField1`) - Ít nhất 6 ký tự
- **Xác nhận mật khẩu** (`jPasswordField2`) - Phải khớp với mật khẩu
- **Email** (`jTextField3`) - Phải đúng định dạng email

**Các trường không bắt buộc:**
- **Số Điện Thoại** (`jTextField4`) - Nếu nhập phải đúng định dạng số Việt Nam
- **Địa Chỉ** (`jTextField5`) - Không bắt buộc

### 2. Form Đặt Hàng (`DatHangJDialog`)

**Các trường bắt buộc:**
- **Họ và tên** (`jTextField3`) - Ít nhất 2 ký tự
- **Số điện thoại** (`jTextField2`) - Phải đúng định dạng số Việt Nam
- **Số nhà** (`jTextField1`) - Ít nhất 5 ký tự
- **Thành phố** (`City` combobox) - Phải chọn
- **Quốc gia** (`Country` combobox) - Phải chọn

### 3. Quản Lý Khách Hàng (`QLKhachHang`)

**Các trường bắt buộc:**
- **Tên Đăng Nhập** - Ít nhất 3 ký tự, chỉ chứa chữ cái, số và dấu gạch dưới
- **Email** - Phải đúng định dạng email
- **Họ Tên** - Ít nhất 2 ký tự

**Các trường không bắt buộc:**
- **Số Điện Thoại** - Nếu nhập phải đúng định dạng số Việt Nam
- **Địa Chỉ** - Không bắt buộc

### 4. Quản Lý Nhà Cung Cấp (`QLNhaCungCap11`)

**Các trường bắt buộc:**
- **Mã nhà cung cấp**
- **Tên nhà cung cấp**
- **Tên liên hệ**
- **Số điện thoại** - Phải đúng định dạng số Việt Nam
- **Email** - Phải đúng định dạng email
- **Địa chỉ**

### 5. Đánh Giá Sản Phẩm (`DanhGiaJDialog1`)

**Các trường bắt buộc:**
- **Số sao đánh giá** - Phải chọn
- **Bình luận** - Ít nhất 10 ký tự

## Hiển Thị Trên Giao Diện

### Dấu * (Asterisk)
- Các trường bắt buộc được đánh dấu bằng dấu **`*`** ở cuối label
- Ví dụ: "Họ Tên: *", "Email: *"

### Tooltip Hướng Dẫn
- Mỗi trường bắt buộc có tooltip hướng dẫn cách nhập
- Ví dụ: "Nhập họ tên đầy đủ (ít nhất 2 ký tự)"

### Thông Báo Lỗi
- Sử dụng emoji và format rõ ràng
- Ví dụ: "❌ Họ tên không được để trống!"

## Utility Class: `RequiredFieldUtil`

### Các Phương Thức Chính

```java
// Thêm dấu * cho label
RequiredFieldUtil.addRequiredIndicator(label, "Tên trường");

// Thêm tooltip
RequiredFieldUtil.addTooltip(textField, "Hướng dẫn nhập");

// Validate trường bắt buộc
String error = RequiredFieldUtil.validateRequiredField(value, "Tên trường", minLength);

// Validate email
boolean isValid = RequiredFieldUtil.isValidEmail(email);

// Validate số điện thoại Việt Nam
boolean isValid = RequiredFieldUtil.isValidVietnamesePhone(phone);

// Validate username
boolean isValid = RequiredFieldUtil.isValidUsername(username);

// Hiển thị lỗi validation
RequiredFieldUtil.showValidationError(parent, errorMessage);
```

### Ví Dụ Sử Dụng

```java
// Thêm dấu * cho các trường bắt buộc
private void addRequiredFieldIndicators() {
    RequiredFieldUtil.addRequiredIndicator(jLabel1, "Họ Tên");
    RequiredFieldUtil.addRequiredIndicator(jLabel2, "Email");
    
    RequiredFieldUtil.addTooltip(jTextField1, "Nhập họ tên đầy đủ");
    RequiredFieldUtil.addTooltip(jTextField2, "Nhập email hợp lệ");
}

// Validate form
private String validateForm() {
    StringBuilder errorBuilder = RequiredFieldUtil.createErrorMessageBuilder();
    
    String nameError = RequiredFieldUtil.validateRequiredField(
        jTextField1.getText().trim(), "Họ tên", 2);
    if (nameError != null) {
        RequiredFieldUtil.addError(errorBuilder, nameError);
    }
    
    if (!RequiredFieldUtil.isValidEmail(jTextField2.getText().trim())) {
        RequiredFieldUtil.addError(errorBuilder, "❌ Email không đúng định dạng!");
    }
    
    return errorBuilder.length() > 0 ? errorBuilder.toString() : null;
}
```

## Quy Tắc Validation

### 1. Họ Tên
- Không được để trống
- Ít nhất 2 ký tự
- Chỉ chứa chữ cái, dấu cách, dấu gạch ngang

### 2. Tên Đăng Nhập
- Không được để trống
- Ít nhất 3 ký tự
- Chỉ chứa chữ cái, số và dấu gạch dưới
- Không được trùng với tên đăng nhập khác

### 3. Email
- Không được để trống
- Phải đúng định dạng email
- Không được trùng với email khác

### 4. Mật Khẩu
- Không được để trống
- Ít nhất 6 ký tự
- Xác nhận mật khẩu phải khớp

### 5. Số Điện Thoại
- Nếu nhập phải đúng định dạng số Việt Nam
- Hỗ trợ các format: 0123456789, 0987654321, +84123456789

### 6. Địa Chỉ
- Nếu bắt buộc: không được để trống, ít nhất 5 ký tự
- Nếu không bắt buộc: có thể để trống

## Cách Thêm Trường Unique Mới

### 1. Thêm Dấu * Trên Giao Diện
```java
RequiredFieldUtil.addRequiredIndicator(label, "Tên trường");
RequiredFieldUtil.addTooltip(textField, "Hướng dẫn nhập");
```

### 2. Thêm Validation
```java
String error = RequiredFieldUtil.validateRequiredField(value, "Tên trường", minLength);
if (error != null) {
    RequiredFieldUtil.addError(errorBuilder, error);
}
```

### 3. Hiển Thị Lỗi
```java
if (errorBuilder.length() > 0) {
    RequiredFieldUtil.showValidationError(this, errorBuilder.toString());
    return;
}
```

## Lưu Ý Quan Trọng

1. **Tính nhất quán**: Tất cả các form phải sử dụng cùng một cách hiển thị dấu *
2. **Thông báo rõ ràng**: Sử dụng emoji và format dễ đọc
3. **Validation real-time**: Có thể thêm validation khi người dùng nhập
4. **Focus tự động**: Tự động focus vào trường có lỗi đầu tiên
5. **Tooltip hữu ích**: Cung cấp hướng dẫn cụ thể cho từng trường

## Kết Luận

Việc sử dụng các trường unique với dấu * và validation chặt chẽ giúp:
- Cải thiện trải nghiệm người dùng
- Giảm thiểu lỗi dữ liệu
- Đảm bảo tính toàn vẹn dữ liệu
- Tăng tính chuyên nghiệp của ứng dụng
