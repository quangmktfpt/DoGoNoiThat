package poly.dao;

import poly.entity.User;

public interface UserDAO extends CrudDAO<User, Integer> {
    // Đăng nhập, trả về User nếu thành công, null nếu thất bại
    User login(String username, String password);
} 