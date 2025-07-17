<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.dao;

import poly.entity.OrderDetail;

/**
 *
 * @author admin
 */
public interface OrderDetailDAO extends CrudDao<OrderDetail, Integer> {

}
=======
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
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
