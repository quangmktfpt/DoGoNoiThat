package poly.controller;

import poly.entity.OrderDetail;
import java.util.List;

public interface OrderDetailController extends CrudController<OrderDetail> {
    // Lấy danh sách chi tiết hóa đơn theo OrderID
    List<OrderDetail> selectByOrderId(int orderId);

    // Xóa toàn bộ chi tiết hóa đơn theo OrderID
    void deleteByOrderId(int orderId);

    // Tính tổng tiền hóa đơn theo OrderID
    double getTotalAmountByOrderId(int orderId);
} 