package poly.dao;

import poly.entity.Order;
import poly.entity.OrderDetail;
import java.util.List;
import java.math.BigDecimal;

public interface CustomerOrderHistoryDAO {
    
    // Lấy lịch sử đơn hàng của một khách hàng
    List<Order> getCustomerOrderHistory(Integer userId);
    
    // Lấy chi tiết đơn hàng của khách hàng
    List<OrderDetail> getCustomerOrderDetails(Integer orderId);
    
    // Lấy tổng giá trị đơn hàng của khách hàng
    BigDecimal getCustomerTotalOrderValue(Integer userId);
    
    // Lấy số lượng đơn hàng của khách hàng
    int getCustomerOrderCount(Integer userId);
    
    // Lấy đơn hàng gần nhất của khách hàng
    Order getCustomerLatestOrder(Integer userId);
    
    // Lấy thống kê đơn hàng theo trạng thái của khách hàng
    List<Object[]> getCustomerOrderStatistics(Integer userId);
} 