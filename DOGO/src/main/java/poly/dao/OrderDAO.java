package poly.dao;

import poly.entity.Order;
import poly.entity.OrderDetail;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public interface OrderDAO extends CrudDAO<Order, Integer> {
    
    // Lấy danh sách đơn hàng theo trạng thái
    List<Order> selectByStatus(String status);
    
    // Lấy danh sách đơn hàng theo khách hàng
    List<Order> selectByUserId(Integer userId);
    
    // Lấy danh sách đơn hàng theo khoảng thời gian
    List<Order> selectByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy danh sách đơn hàng theo phương thức thanh toán
    List<Order> selectByPaymentMethod(String paymentMethod);
    
    // Tìm kiếm đơn hàng theo từ khóa (OrderID, UserID, PaymentMethod)
    List<Order> searchByKeyword(String keyword);
    
    // Cập nhật trạng thái đơn hàng
    void updateOrderStatus(Integer orderId, String status);
    
    // Lấy tổng doanh thu theo khoảng thời gian
    BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy số lượng đơn hàng theo trạng thái
    int getOrderCountByStatus(String status);
    
    // Lấy chi tiết đơn hàng
    List<OrderDetail> getOrderDetails(Integer orderId);
    
    // Thêm chi tiết đơn hàng
    void insertOrderDetail(OrderDetail orderDetail);
    
    // Xóa chi tiết đơn hàng
    void deleteOrderDetail(Integer orderDetailId);
    
    // Lấy thông tin đơn hàng với thông tin khách hàng
    List<Order> selectOrdersWithCustomerInfo();
    
    // Cập nhật trạng thái đơn hàng với lý do và cập nhật tồn kho
    void updateOrderStatusWithReasonAndInventory(Integer orderId, String status, String returnReason);
} 