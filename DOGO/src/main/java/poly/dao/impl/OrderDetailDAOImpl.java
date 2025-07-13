package poly.dao.impl;

import poly.dao.OrderDetailDAO;
import poly.entity.OrderDetail;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    
    private final String INSERT_SQL = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE OrderDetails SET OrderID=?, ProductID=?, Quantity=?, UnitPrice=? WHERE OrderDetailID=?";
    private final String DELETE_SQL = "DELETE FROM OrderDetails WHERE OrderDetailID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM OrderDetails";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM OrderDetails WHERE OrderDetailID=?";
    private final String SELECT_BY_ORDER_SQL = "SELECT * FROM OrderDetails WHERE OrderID=?";
    private final String SELECT_BY_PRODUCT_SQL = "SELECT * FROM OrderDetails WHERE ProductID=?";
    private final String DELETE_BY_ORDER_SQL = "DELETE FROM OrderDetails WHERE OrderID=?";
    private final String GET_TOTAL_QUANTITY_SQL = "SELECT SUM(Quantity) FROM OrderDetails WHERE ProductID=?";

    @Override
    public void insert(OrderDetail orderDetail) {
        XJdbc.executeUpdate(INSERT_SQL, 
            orderDetail.getOrderId(), 
            orderDetail.getProductId(), 
            orderDetail.getQuantity(), 
            orderDetail.getUnitPrice()
        );
    }

    @Override
    public void update(OrderDetail orderDetail) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            orderDetail.getOrderId(), 
            orderDetail.getProductId(), 
            orderDetail.getQuantity(), 
            orderDetail.getUnitPrice(), 
            orderDetail.getOrderDetailId()
        );
    }

    @Override
    public void delete(Integer orderDetailId) {
        XJdbc.executeUpdate(DELETE_SQL, orderDetailId);
    }

    @Override
    public List<OrderDetail> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public OrderDetail selectById(Integer orderDetailId) {
        List<OrderDetail> list = selectBySql(SELECT_BY_ID_SQL, orderDetailId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OrderDetail> selectByOrderId(Integer orderId) {
        return selectBySql(SELECT_BY_ORDER_SQL, orderId);
    }

    @Override
    public List<OrderDetail> selectByProductId(String productId) {
        return selectBySql(SELECT_BY_PRODUCT_SQL, productId);
    }

    @Override
    public void deleteByOrderId(Integer orderId) {
        XJdbc.executeUpdate(DELETE_BY_ORDER_SQL, orderId);
    }

    @Override
    public int getTotalQuantitySold(String productId) {
        try {
            ResultSet rs = XJdbc.executeQuery(GET_TOTAL_QUANTITY_SQL, productId);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public List<OrderDetail> selectBySql(String sql, Object... args) {
        List<OrderDetail> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getInt("OrderDetailID"));
                orderDetail.setOrderId(rs.getInt("OrderID"));
                orderDetail.setProductId(rs.getString("ProductID"));
                orderDetail.setQuantity(rs.getInt("Quantity"));
                orderDetail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                list.add(orderDetail);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 