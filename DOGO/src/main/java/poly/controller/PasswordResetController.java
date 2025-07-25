package poly.controller;

public interface PasswordResetController {
    // Gửi mã xác minh về email, trả về true nếu thành công
    boolean sendVerificationCode(String email, String username);

    // Xác nhận mã xác minh, trả về true nếu hợp lệ
    boolean verifyCode(String email, String username, String code);

    // Đổi mật khẩu mới khi mã xác minh hợp lệ
    boolean resetPassword(String email, String username, String newPassword);
} 