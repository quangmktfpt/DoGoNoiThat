package poly.dao;

import poly.entity.User;
import java.util.List;

public interface UserDAO extends CrudDAO<User, Integer> {
    // Đăng nhập, trả về User nếu thành công, null nếu thất bại
    User login(String username, String password);

    // Tìm user theo email
    User findByEmail(String email);

    // Tìm user theo username và email
    User findByUsernameAndEmail(String username, String email);

    // Cập nhật mật khẩu theo userID
    boolean updatePassword(int userID, String newPasswordHash);
} 