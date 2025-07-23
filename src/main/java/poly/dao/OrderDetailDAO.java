package poly.dao;

import poly.entity.OrderDetail;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail, Integer> {
    
    // Lấy tất cả chi tiết đơn hàng theo OrderID
    List<OrderDetail> selectByOrderId(Integer orderId);
    
    // Lấy chi tiết đơn hàng theo ProductID
    List<OrderDetail> selectByProductId(String productId);
    
    // Xóa tất cả chi tiết đơn hàng theo OrderID
    void deleteByOrderId(Integer orderId);
    
    // Lấy tổng số lượng sản phẩm đã bán theo ProductID
    int getTotalQuantitySold(String productId);
} 