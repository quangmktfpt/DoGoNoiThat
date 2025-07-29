package poly.dao.impl;

import poly.dao.OrderDAO;
import poly.entity.Order;
import poly.entity.OrderDetail;
import poly.entity.User;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class OrderDAOImpl implements OrderDAO {
    
    private final String INSERT_SQL = "INSERT INTO Orders (UserID, OrderDate, TotalAmount, CouponID, PaymentMethod, OrderStatus, IsActive, DeliveryAddressID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Orders SET UserID=?, OrderDate=?, TotalAmount=?, CouponID=?, PaymentMethod=?, OrderStatus=?, IsActive=?, DeliveryAddressID=? WHERE OrderID=?";
    private final String DELETE_SQL = "DELETE FROM Orders WHERE OrderID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Orders ORDER BY OrderDate DESC";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Orders WHERE OrderID=?";
    private final String SELECT_BY_STATUS_SQL = "SELECT * FROM Orders WHERE OrderStatus=? ORDER BY OrderDate DESC";
    private final String SELECT_BY_USER_SQL = "SELECT * FROM Orders WHERE UserID=? ORDER BY OrderDate DESC";
    private final String SELECT_BY_DATE_RANGE_SQL = "SELECT * FROM Orders WHERE OrderDate BETWEEN ? AND ? ORDER BY OrderDate DESC";
    private final String SELECT_BY_PAYMENT_SQL = "SELECT * FROM Orders WHERE PaymentMethod=? ORDER BY OrderDate DESC";
    private final String SEARCH_BY_KEYWORD_SQL = "SELECT * FROM Orders WHERE CAST(OrderID AS VARCHAR) LIKE ? OR CAST(UserID AS VARCHAR) LIKE ? OR PaymentMethod LIKE ? ORDER BY OrderDate DESC";
    private final String UPDATE_STATUS_SQL = "UPDATE Orders SET OrderStatus=? WHERE OrderID=?";
    private final String UPDATE_STATUS_WITH_REASON_SQL = "UPDATE Orders SET OrderStatus=?, ReturnReason=? WHERE OrderID=?";
    private final String GET_TOTAL_REVENUE_SQL = "SELECT SUM(TotalAmount) FROM Orders WHERE OrderDate BETWEEN ? AND ? AND OrderStatus='Completed'";
    private final String GET_ORDER_COUNT_SQL = "SELECT COUNT(*) FROM Orders WHERE OrderStatus=?";
    private final String SELECT_WITH_CUSTOMER_SQL = "SELECT o.*, u.FullName, u.Phone, u.Email FROM Orders o LEFT JOIN Users u ON o.UserID = u.UserID ORDER BY o.OrderDate DESC";

    @Override
    public void insert(Order order) {
        XJdbc.executeUpdate(INSERT_SQL, 
            order.getUserId(), 
            order.getOrderDate(), 
            order.getTotalAmount(), 
            order.getCouponId(), 
            order.getPaymentMethod(), 
            order.getOrderStatus(), 
            order.getIsActive(), 
            order.getDeliveryAddressId()
        );
    }

    @Override
    public void update(Order order) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            order.getUserId(), 
            order.getOrderDate(), 
            order.getTotalAmount(), 
            order.getCouponId(), 
            order.getPaymentMethod(), 
            order.getOrderStatus(), 
            order.getIsActive(), 
            order.getDeliveryAddressId(), 
            order.getOrderId()
        );
    }

    @Override
    public void delete(Integer orderId) {
        XJdbc.executeUpdate(DELETE_SQL, orderId);
    }

    @Override
    public List<Order> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public Order selectById(Integer orderId) {
        List<Order> list = selectBySql(SELECT_BY_ID_SQL, orderId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Order> selectByStatus(String status) {
        return selectBySql(SELECT_BY_STATUS_SQL, status);
    }

    @Override
    public List<Order> selectByUserId(Integer userId) {
        return selectBySql(SELECT_BY_USER_SQL, userId);
    }

    @Override
    public List<Order> selectByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return selectBySql(SELECT_BY_DATE_RANGE_SQL, startDate, endDate);
    }

    @Override
    public List<Order> selectByPaymentMethod(String paymentMethod) {
        return selectBySql(SELECT_BY_PAYMENT_SQL, paymentMethod);
    }

    @Override
    public List<Order> searchByKeyword(String keyword) {
        String searchPattern = "%" + keyword + "%";
        return selectBySql(SEARCH_BY_KEYWORD_SQL, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public void updateOrderStatus(Integer orderId, String status) {
        XJdbc.executeUpdate(UPDATE_STATUS_SQL, status, orderId);
    }

    public void updateOrderStatusWithReason(Integer orderId, String status, String returnReason) {
        XJdbc.executeUpdate(UPDATE_STATUS_WITH_REASON_SQL, status, returnReason, orderId);
    }

    @Override
    public void updateOrderStatusWithReasonAndInventory(Integer orderId, String status, String returnReason) {
        try {
            System.out.println("=== DEBUG: Bắt đầu xử lý đơn hàng " + orderId + " ===");
            
            // Kiểm tra trạng thái hiện tại để tránh cập nhật nhiều lần
            Order currentOrder = selectById(orderId);
            if (currentOrder == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId);
            }
            
            System.out.println("DEBUG: Trạng thái hiện tại: " + currentOrder.getOrderStatus());
            System.out.println("DEBUG: Lý do hiện tại: " + currentOrder.getReturnReason());
            
            // Chỉ cập nhật nếu trạng thái hiện tại chưa phải là Cancelled
            // Và kiểm tra xem đã có lý do đổi trả/huỷ chưa để tránh xử lý nhiều lần
            if (!"Cancelled".equals(currentOrder.getOrderStatus()) || 
                (currentOrder.getReturnReason() == null || currentOrder.getReturnReason().trim().isEmpty())) {
                System.out.println("DEBUG: Bắt đầu cập nhật trạng thái và tồn kho");
                
                // Cập nhật trạng thái đơn hàng
                updateOrderStatusWithReason(orderId, status, returnReason);
                
                // Lấy chi tiết đơn hàng để cập nhật tồn kho
                List<OrderDetail> orderDetails = getOrderDetails(orderId);
                System.out.println("DEBUG: Số lượng sản phẩm trong đơn hàng: " + orderDetails.size());
                
                // Cập nhật tồn kho cho từng sản phẩm
                for (OrderDetail detail : orderDetails) {
                    // Debug: In ra thông tin để kiểm tra
                    System.out.println("DEBUG: Cập nhật tồn kho - ProductID: " + detail.getProductId() + 
                                     ", Quantity: " + detail.getQuantity() + 
                                     ", OrderID: " + orderId);
                    
                    // Kiểm tra tồn kho hiện tại trước khi cập nhật
                    String checkInventorySQL = "SELECT Quantity FROM Products WHERE ProductID = ?";
                    java.sql.ResultSet rs = XJdbc.executeQuery(checkInventorySQL, detail.getProductId());
                    int currentQuantity = 0;
                    if (rs.next()) {
                        currentQuantity = rs.getInt("Quantity");
                    }
                    System.out.println("DEBUG: Tồn kho hiện tại của " + detail.getProductId() + ": " + currentQuantity);
                    
                    // Kiểm tra xem có trigger nào không
                    System.out.println("DEBUG: Kiểm tra trigger - trước khi UPDATE");
                    
                    // Thêm debug để kiểm tra trigger
                    System.out.println("DEBUG: Bắt đầu UPDATE Products SET Quantity = Quantity + " + detail.getQuantity() + " WHERE ProductID = " + detail.getProductId());
                    
                    // SỬ DỤNG CÁCH KHÁC - Cập nhật trực tiếp không qua trigger
                    System.out.println("DEBUG: Sử dụng cách cập nhật trực tiếp");
                    
                    // Lấy tồn kho hiện tại
                    String getCurrentQuantitySQL = "SELECT Quantity FROM Products WHERE ProductID = ?";
                    java.sql.ResultSet rsCurrent = XJdbc.executeQuery(getCurrentQuantitySQL, detail.getProductId());
                    int currentQuantityDirect = 0;
                    if (rsCurrent.next()) {
                        currentQuantityDirect = rsCurrent.getInt("Quantity");
                    }
                    
                    // Hệ thống tự động cộng tồn kho khi đơn hàng bị huỷ/đổi trả
                    System.out.println("DEBUG: Hệ thống tự động cộng tồn kho - không cần code thêm");
                    
                    System.out.println("DEBUG: Hoàn thành UPDATE");
                    
                    // Kiểm tra tồn kho sau khi cập nhật
                    rs = XJdbc.executeQuery(checkInventorySQL, detail.getProductId());
                    int newQuantity = 0;
                    if (rs.next()) {
                        newQuantity = rs.getInt("Quantity");
                    }
                    System.out.println("DEBUG: Tồn kho sau khi cập nhật của " + detail.getProductId() + ": " + newQuantity);
                    System.out.println("DEBUG: Số lượng đã cộng: " + detail.getQuantity());
                    System.out.println("DEBUG: Kết quả mong đợi: " + (currentQuantity + detail.getQuantity()));
                    System.out.println("DEBUG: Kết quả thực tế: " + newQuantity);
                    
                    if (newQuantity != (currentQuantity + detail.getQuantity())) {
                        System.out.println("DEBUG: ⚠️ CÓ LẠM PHÁT! Trigger có thể đang hoạt động!");
                        System.out.println("DEBUG: Chênh lệch: " + (newQuantity - (currentQuantity + detail.getQuantity())));
                    }
                    
                    // Ghi log giao dịch tồn kho
                    String insertTransactionSQL = "INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes) VALUES (?, ?, ?, ?, ?)";
                    String transactionType = "Adjustment"; // Hoặc có thể dùng 'ReturnIn' nếu muốn
                    String referenceID = "ORDER-" + orderId;
                    String notes = "Hoàn trả sản phẩm từ đơn hàng " + orderId + " - " + returnReason;
                    XJdbc.executeUpdate(insertTransactionSQL, detail.getProductId(), transactionType, detail.getQuantity(), referenceID, notes);
                }
                
                System.out.println("DEBUG: Hoàn thành cập nhật đơn hàng " + orderId);
            } else {
                System.out.println("DEBUG: Bỏ qua cập nhật vì đơn hàng đã được xử lý");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật đơn hàng và tồn kho: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_TOTAL_REVENUE_SQL, startDate, endDate);
            if (rs.next()) {
                BigDecimal result = rs.getBigDecimal(1);
                return result != null ? result : BigDecimal.ZERO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int getOrderCountByStatus(String status) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_ORDER_COUNT_SQL, status);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public List<OrderDetail> getOrderDetails(Integer orderId) {
        OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl();
        return orderDetailDAO.selectByOrderId(orderId);
    }

    @Override
    public void insertOrderDetail(OrderDetail orderDetail) {
        OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl();
        orderDetailDAO.insert(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Integer orderDetailId) {
        OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl();
        orderDetailDAO.delete(orderDetailId);
    }

    @Override
    public List<Order> selectOrdersWithCustomerInfo() {
        return selectBySql(SELECT_WITH_CUSTOMER_SQL);
    }

    @Override
    public List<Order> selectBySql(String sql, Object... args) {
        List<Order> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setUserId(rs.getInt("UserID"));
                order.setOrderDate(rs.getTimestamp("OrderDate") != null ? rs.getTimestamp("OrderDate").toLocalDateTime() : null);
                order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                order.setCouponId(rs.getString("CouponID"));
                order.setPaymentMethod(rs.getString("PaymentMethod"));
                order.setOrderStatus(rs.getString("OrderStatus"));
                order.setIsActive(rs.getObject("IsActive") != null ? rs.getBoolean("IsActive") : null);
                order.setDeliveryAddressId(rs.getObject("DeliveryAddressID") != null ? rs.getInt("DeliveryAddressID") : null);
                order.setReturnReason(rs.getString("ReturnReason"));
                list.add(order);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 