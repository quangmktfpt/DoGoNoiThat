package poly.dao;

import poly.entity.OrderRequest;
import poly.entity.OrderRequestItem;
import java.util.List;
import java.math.BigDecimal;

/**
 * Interface DAO cho OrderRequest
 * @author Nghia
 */
public interface OrderRequestDAO extends CrudDAO<OrderRequest, Integer> {
    
    // Lấy danh sách đơn hàng theo trạng thái
    List<OrderRequest> selectByStatus(String status);
    
    // Lấy danh sách đơn hàng theo khách hàng
    List<OrderRequest> selectByUserId(Integer userId);
    
    // Lấy danh sách đơn hàng theo phương thức thanh toán
    List<OrderRequest> selectByPaymentMethod(String paymentMethod);
    
    // Tìm kiếm đơn hàng theo từ khóa
    List<OrderRequest> searchByKeyword(String keyword);
    
    // Cập nhật trạng thái đơn hàng
    void updateOrderStatus(Integer orderId, String status);
    
    // Lấy tổng doanh thu theo khoảng thời gian
    BigDecimal getTotalRevenue(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    // Lấy số lượng đơn hàng theo trạng thái
    int getOrderCountByStatus(String status);
    
    // Lấy chi tiết đơn hàng
    List<OrderRequestItem> getOrderRequestItems(Integer orderId);
    
    // Thêm chi tiết đơn hàng
    void insertOrderRequestItem(OrderRequestItem item);
    
    // Xóa chi tiết đơn hàng
    void deleteOrderRequestItem(Integer itemId);
    
    // Lấy thông tin đơn hàng với thông tin khách hàng
    List<OrderRequest> selectOrdersWithCustomerInfo();
    
    // Tạo đơn hàng mới từ giỏ hàng
    OrderRequest createOrderFromCart(Integer userId, String customerName, String phone, 
                                   String address, String city, String country, 
                                   String paymentMethod, String couponId);
    
    // Tính toán phí vận chuyển
    BigDecimal calculateShippingFee(String city, String country);
    
    // Tính toán giảm giá từ mã coupon
    BigDecimal calculateDiscount(String couponId, BigDecimal subtotal);
    
    // Kiểm tra tính hợp lệ của đơn hàng
    boolean validateOrder(OrderRequest order);
} 