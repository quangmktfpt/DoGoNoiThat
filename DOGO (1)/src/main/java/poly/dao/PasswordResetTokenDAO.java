package poly.dao;

import poly.entity.PasswordResetToken;
import java.util.List;

public interface PasswordResetTokenDAO {
    // Thêm mới token
    boolean insert(PasswordResetToken token);

    // Lấy token hợp lệ theo userID và mã token (chưa hết hạn, chưa dùng)
    PasswordResetToken findValidToken(int userID, String token);

    // Đánh dấu token đã dùng
    boolean markTokenAsUsed(int tokenID);

    // Xóa các token hết hạn hoặc đã dùng (tùy chọn)
    int deleteExpiredOrUsedTokens();

    // (Tùy chọn) Lấy danh sách token của 1 user
    List<PasswordResetToken> findTokensByUser(int userID);
} 