package poly.dao.impl;

import poly.dao.CustomerOrderHistoryDAO;
import poly.entity.Order;
import poly.entity.OrderDetail;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class CustomerOrderHistoryDAOImpl implements CustomerOrderHistoryDAO {
    
    private final String GET_CUSTOMER_ORDER_HISTORY_SQL = 
        "SELECT * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
    
    private final String GET_CUSTOMER_ORDER_DETAILS_SQL = 
        "SELECT od.*, p.ProductName FROM OrderDetails od " +
        "LEFT JOIN Products p ON od.ProductID = p.ProductID " +
        "WHERE od.OrderID = ?";
    
    private final String GET_CUSTOMER_TOTAL_ORDER_VALUE_SQL = 
        "SELECT COALESCE(SUM(TotalAmount), 0) FROM Orders WHERE UserID = ? AND OrderStatus = 'Completed'";
    
    private final String GET_CUSTOMER_ORDER_COUNT_SQL = 
        "SELECT COUNT(*) FROM Orders WHERE UserID = ?";
    
    private final String GET_CUSTOMER_LATEST_ORDER_SQL = 
        "SELECT TOP 1 * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
    
    private final String GET_CUSTOMER_ORDER_STATISTICS_SQL = 
        "SELECT OrderStatus, COUNT(*) as Count, SUM(TotalAmount) as TotalAmount " +
        "FROM Orders WHERE UserID = ? GROUP BY OrderStatus";

    @Override
    public List<Order> getCustomerOrderHistory(Integer userId) {
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_ORDER_HISTORY_SQL, userId);
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
                orders.add(order);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<OrderDetail> getCustomerOrderDetails(Integer orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_ORDER_DETAILS_SQL, orderId);
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getInt("OrderDetailID"));
                orderDetail.setOrderId(rs.getInt("OrderID"));
                orderDetail.setProductId(rs.getString("ProductID"));
                orderDetail.setQuantity(rs.getInt("Quantity"));
                orderDetail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                orderDetails.add(orderDetail);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orderDetails;
    }

    @Override
    public BigDecimal getCustomerTotalOrderValue(Integer userId) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_TOTAL_ORDER_VALUE_SQL, userId);
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
    public int getCustomerOrderCount(Integer userId) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_ORDER_COUNT_SQL, userId);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public Order getCustomerLatestOrder(Integer userId) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_LATEST_ORDER_SQL, userId);
            if (rs.next()) {
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
                return order;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Object[]> getCustomerOrderStatistics(Integer userId) {
        List<Object[]> statistics = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(GET_CUSTOMER_ORDER_STATISTICS_SQL, userId);
            while (rs.next()) {
                Object[] stat = new Object[3];
                stat[0] = rs.getString("OrderStatus");
                stat[1] = rs.getInt("Count");
                stat[2] = rs.getBigDecimal("TotalAmount");
                statistics.add(stat);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return statistics;
    }
} 