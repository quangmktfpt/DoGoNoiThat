package poly.controller;

import poly.entity.User;
import poly.entity.Order;
import java.util.List;
import java.math.BigDecimal;

public interface UserController extends CrudController<User> {
    
    // Lấy danh sách khách hàng (role = 0)
    List<User> loadCustomers();
    
    // Lấy danh sách khách hàng đang hoạt động
    List<User> loadActiveCustomers();
    
    // Tìm kiếm khách hàng theo từ khóa
    List<User> searchCustomers(String keyword);
    
    // Kiểm tra username đã tồn tại chưa
    boolean checkUsernameExists(String username);
    
    // Kiểm tra email đã tồn tại chưa
    boolean checkEmailExists(String email);
    
    // Cập nhật trạng thái hoạt động của khách hàng
    void updateCustomerStatus(Integer userId, Boolean isActive);
    
    // Đặt lại mật khẩu cho khách hàng
    void resetCustomerPassword(Integer userId, String newPassword);
    
    // Lấy lịch sử đơn hàng của khách hàng
    List<Order> getCustomerOrderHistory(Integer userId);
    
    // Lấy tổng giá trị đơn hàng của khách hàng
    BigDecimal getCustomerTotalOrderValue(Integer userId);
    
    // Lấy số lượng đơn hàng của khách hàng
    int getCustomerOrderCount(Integer userId);
    
    // Lấy thống kê khách hàng theo trạng thái
    int getCustomerCountByStatus(Boolean isActive);
    
    // Validate thông tin khách hàng
    String validateCustomerInfo(User user);
    
    // Mã hóa mật khẩu
    String hashPassword(String password);
} 