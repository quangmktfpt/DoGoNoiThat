package poly.dao.impl;

import poly.dao.OrderRequestDAO;
import poly.dao.CouponDAO;
import poly.dao.ShoppingCartDAO;
import poly.dao.AddressDAO;
import poly.dao.impl.CouponDAOImpl;
import poly.dao.impl.ShoppingCartDAOImpl;
import poly.dao.impl.AddressDAOImpl;
import poly.entity.OrderRequest;
import poly.entity.OrderRequestItem;
import poly.entity.Coupon;
import poly.entity.CartItem;
import poly.entity.Product;
import poly.entity.Address;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import poly.entity.ShoppingCart;

/**
 * Implementation của OrderRequestDAO
 * @author Nghia
 */
public class OrderRequestDAOImpl implements OrderRequestDAO {
    
    private final String INSERT_SQL = "INSERT INTO Orders (UserID, OrderDate, TotalAmount, CouponID, PaymentMethod, OrderStatus, IsActive, DeliveryAddressID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Orders SET UserID=?, OrderDate=?, TotalAmount=?, CouponID=?, PaymentMethod=?, OrderStatus=?, IsActive=?, DeliveryAddressID=? WHERE OrderID=?";
    private final String DELETE_SQL = "DELETE FROM Orders WHERE OrderID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Orders ORDER BY OrderDate DESC";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Orders WHERE OrderID=?";
    private final String SELECT_BY_STATUS_SQL = "SELECT * FROM Orders WHERE OrderStatus=? ORDER BY OrderDate DESC";
    private final String SELECT_BY_USER_SQL = "SELECT * FROM Orders WHERE UserID=? ORDER BY OrderDate DESC";
    private final String SELECT_BY_PAYMENT_SQL = "SELECT * FROM Orders WHERE PaymentMethod=? ORDER BY OrderDate DESC";
    private final String SEARCH_BY_KEYWORD_SQL = "SELECT * FROM Orders WHERE CAST(OrderID AS VARCHAR) LIKE ? OR CAST(UserID AS VARCHAR) LIKE ? OR PaymentMethod LIKE ? ORDER BY OrderDate DESC";
    private final String UPDATE_STATUS_SQL = "UPDATE Orders SET OrderStatus=? WHERE OrderID=?";
    private final String GET_TOTAL_REVENUE_SQL = "SELECT SUM(TotalAmount) FROM Orders WHERE OrderDate BETWEEN ? AND ? AND OrderStatus='Completed'";
    private final String GET_ORDER_COUNT_SQL = "SELECT COUNT(*) FROM Orders WHERE OrderStatus=?";
    private final String SELECT_WITH_CUSTOMER_SQL = "SELECT o.*, u.FullName, u.Phone, u.Email FROM Orders o LEFT JOIN Users u ON o.UserID = u.UserID ORDER BY o.OrderDate DESC";
    
    private final String INSERT_ORDER_ITEM_SQL = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
    private final String SELECT_ORDER_ITEMS_SQL = "SELECT od.*, p.ProductName FROM OrderDetails od LEFT JOIN Products p ON od.ProductID = p.ProductID WHERE od.OrderID=?";
    private final String DELETE_ORDER_ITEM_SQL = "DELETE FROM OrderDetails WHERE OrderDetailID=?";
    
    private final String INSERT_ADDRESS_SQL = "INSERT INTO Addresses (UserID, AddressLine1, City, Country, Phone, CustomerName, IsDefault) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_ADDRESS_BY_USER_SQL = "SELECT * FROM Addresses WHERE UserID=? ORDER BY IsDefault DESC";
    
    private CouponDAO couponDAO = new CouponDAOImpl();
    private ShoppingCartDAO cartDAO = new ShoppingCartDAOImpl();
    private AddressDAO addressDAO = new AddressDAOImpl();

    @Override
    public void insert(OrderRequest orderRequest) {
        try {
            // Đảm bảo user có ít nhất một địa chỉ mặc định
            ensureUserHasDefaultAddress(orderRequest.getUserId());
            
            // Tạo địa chỉ giao hàng trước
            Integer addressId = createDeliveryAddress(orderRequest);
            
            if (addressId == null) {
                throw new RuntimeException("Không thể tạo địa chỉ giao hàng");
            }
            
            // Insert đơn hàng
            XJdbc.executeUpdate(INSERT_SQL, 
                orderRequest.getUserId(), 
                orderRequest.getOrderDate(), 
                orderRequest.getTotalAmount(), 
                orderRequest.getCouponId(), 
                orderRequest.getPaymentMethod(), 
                orderRequest.getOrderStatus(), 
                true, // IsActive
                addressId
            );
            
            // Lấy OrderID vừa tạo
            Integer orderId = getLastInsertedOrderId();
            if (orderId == null) {
                throw new RuntimeException("Không thể lấy OrderID vừa tạo");
            }
            
            orderRequest.setOrderId(orderId);
            
            // Insert các item trong đơn hàng
            if (orderRequest.getItems() != null) {
                for (OrderRequestItem item : orderRequest.getItems()) {
                    item.setOrderId(orderId); // Set orderId cho item
                    insertOrderRequestItem(item);
                }
            }
            
            System.out.println("✓ Đã tạo đơn hàng thành công:");
            System.out.println("  - OrderID: " + orderId);
            System.out.println("  - AddressID: " + addressId);
            System.out.println("  - UserID: " + orderRequest.getUserId());
            System.out.println("  - TotalAmount: " + orderRequest.getTotalAmount());
            
        } catch (Exception e) {
            System.err.println("✗ Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo đơn hàng: " + e.getMessage());
        }
    }

    @Override
    public void update(OrderRequest orderRequest) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            orderRequest.getUserId(), 
            orderRequest.getOrderDate(), 
            orderRequest.getTotalAmount(), 
            orderRequest.getCouponId(), 
            orderRequest.getPaymentMethod(), 
            orderRequest.getOrderStatus(), 
            true, // IsActive
            orderRequest.getDeliveryAddressId(), 
            orderRequest.getOrderId()
        );
    }

    @Override
    public void delete(Integer orderId) {
        // Xóa các item trước
        deleteOrderRequestItems(orderId);
        // Xóa đơn hàng
        XJdbc.executeUpdate(DELETE_SQL, orderId);
    }

    @Override
    public List<OrderRequest> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public OrderRequest selectById(Integer orderId) {
        List<OrderRequest> list = selectBySql(SELECT_BY_ID_SQL, orderId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OrderRequest> selectByStatus(String status) {
        return selectBySql(SELECT_BY_STATUS_SQL, status);
    }

    @Override
    public List<OrderRequest> selectByUserId(Integer userId) {
        return selectBySql(SELECT_BY_USER_SQL, userId);
    }

    @Override
    public List<OrderRequest> selectByPaymentMethod(String paymentMethod) {
        return selectBySql(SELECT_BY_PAYMENT_SQL, paymentMethod);
    }

    @Override
    public List<OrderRequest> searchByKeyword(String keyword) {
        String searchPattern = "%" + keyword + "%";
        return selectBySql(SEARCH_BY_KEYWORD_SQL, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public void updateOrderStatus(Integer orderId, String status) {
        XJdbc.executeUpdate(UPDATE_STATUS_SQL, status, orderId);
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
    public List<OrderRequestItem> getOrderRequestItems(Integer orderId) {
        List<OrderRequestItem> items = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(SELECT_ORDER_ITEMS_SQL, orderId);
            while (rs.next()) {
                OrderRequestItem item = new OrderRequestItem();
                item.setItemId(rs.getInt("OrderDetailID"));
                item.setProductId(rs.getString("ProductID"));
                item.setProductName(rs.getString("ProductName"));
                item.setQuantity(rs.getInt("Quantity"));
                item.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                item.calculateTotalPrice();
                items.add(item);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public void insertOrderRequestItem(OrderRequestItem item) {
        // Validate dữ liệu trước khi insert
        if (item.getOrderId() == null) {
            throw new RuntimeException("OrderID không được null");
        }
        if (item.getProductId() == null || item.getProductId().trim().isEmpty()) {
            throw new RuntimeException("ProductID không được null hoặc rỗng");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new RuntimeException("Quantity phải lớn hơn 0");
        }
        if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("UnitPrice phải lớn hơn 0");
        }
        
        System.out.println("✓ Inserting OrderDetail:");
        System.out.println("  - OrderID: " + item.getOrderId());
        System.out.println("  - ProductID: " + item.getProductId());
        System.out.println("  - Quantity: " + item.getQuantity());
        System.out.println("  - UnitPrice: " + item.getUnitPrice());
        
        XJdbc.executeUpdate(INSERT_ORDER_ITEM_SQL, 
            item.getOrderId(), 
            item.getProductId(), 
            item.getQuantity(), 
            item.getUnitPrice()
        );
    }

    @Override
    public void deleteOrderRequestItem(Integer itemId) {
        XJdbc.executeUpdate(DELETE_ORDER_ITEM_SQL, itemId);
    }

    @Override
    public List<OrderRequest> selectOrdersWithCustomerInfo() {
        return selectBySql(SELECT_WITH_CUSTOMER_SQL);
    }

    @Override
    public OrderRequest createOrderFromCart(Integer userId, String customerName, String phone, 
                                          String address, String city, String country, 
                                          String paymentMethod, String couponId) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setCustomerName(customerName);
        orderRequest.setPhone(phone);
        orderRequest.setAddress(address);
        orderRequest.setCity(city);
        orderRequest.setCountry(country);
        orderRequest.setPaymentMethod(paymentMethod);
        orderRequest.setCouponId(couponId);
        orderRequest.setOrderDate(LocalDateTime.now());
        orderRequest.setOrderStatus("Pending");
        
        // Tính toán các giá trị
        BigDecimal subtotal = calculateCartSubtotal(userId);
        BigDecimal shippingFee = calculateShippingFee(city, country);
        BigDecimal discount = calculateDiscount(couponId, subtotal);
        
        orderRequest.setSubtotal(subtotal);
        orderRequest.setShippingFee(shippingFee);
        orderRequest.setDiscount(discount);
        orderRequest.calculateTotal();
        
        // Tạo danh sách OrderRequestItem từ giỏ hàng
        List<OrderRequestItem> orderItems = new ArrayList<>();
        try {
            ShoppingCart cart = cartDAO.findByUserId(userId);
            if (cart != null) {
                List<CartItem> cartItems = cartDAO.findCartItemsByCartId(cart.getCartId());
                for (CartItem cartItem : cartItems) {
                    Product product = getProductById(cartItem.getProductId());
                    if (product != null) {
                        OrderRequestItem orderItem = new OrderRequestItem();
                        orderItem.setProductId(cartItem.getProductId());
                        orderItem.setProductName(product.getProductName());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setUnitPrice(product.getUnitPrice());
                        orderItem.calculateTotalPrice();
                        orderItem.setCity(city);
                        orderItem.setCountry(country);
                        orderItem.setCustomerName(customerName);
                        orderItem.setAddress(address);
                        orderItem.setPhone(phone);
                        orderItem.setPaymentMethod(paymentMethod);
                        orderItems.add(orderItem);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo OrderRequestItem từ giỏ hàng: " + e.getMessage());
        }
        
        orderRequest.setItems(orderItems);
        
        return orderRequest;
    }

    @Override
    public BigDecimal calculateShippingFee(String city, String country) {
        // Logic tính phí vận chuyển đơn giản
        BigDecimal baseFee = new BigDecimal("30000"); // 30,000 VND cơ bản
        
        if ("Vietnam".equalsIgnoreCase(country)) {
            if ("Ho Chi Minh City".equalsIgnoreCase(city) || "Ha Noi".equalsIgnoreCase(city)) {
                return new BigDecimal("15000"); // 15,000 VND cho TP lớn
            } else {
                return new BigDecimal("25000"); // 25,000 VND cho tỉnh khác
            }
        } else {
            return new BigDecimal("100000"); // 100,000 VND cho nước ngoài
        }
    }

    @Override
    public BigDecimal calculateDiscount(String couponId, BigDecimal subtotal) {
        if (couponId == null || couponId.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        Coupon coupon = couponDAO.selectById(couponId);
        if (coupon == null || !couponDAO.isValidCoupon(couponId)) {
            return BigDecimal.ZERO;
        }
        
        if ("Percent".equalsIgnoreCase(coupon.getDiscountType())) {
            return subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
        } else if ("Fixed".equalsIgnoreCase(coupon.getDiscountType())) {
            return coupon.getDiscountValue();
        }
        
        return BigDecimal.ZERO;
    }

    @Override
    public boolean validateOrder(OrderRequest order) {
        if (order.getUserId() == null || order.getCustomerName() == null || 
            order.getCustomerName().trim().isEmpty() || order.getPhone() == null || 
            order.getPhone().trim().isEmpty()) {
            return false;
        }
        
        if (order.getSubtotal() == null || order.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return false;
        }
        
        return true;
    }

    @Override
    public List<OrderRequest> selectBySql(String sql, Object... args) {
        List<OrderRequest> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setOrderId(rs.getInt("OrderID"));
                orderRequest.setUserId(rs.getInt("UserID"));
                orderRequest.setOrderDate(rs.getTimestamp("OrderDate") != null ? rs.getTimestamp("OrderDate").toLocalDateTime() : null);
                orderRequest.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                orderRequest.setCouponId(rs.getString("CouponID"));
                orderRequest.setPaymentMethod(rs.getString("PaymentMethod"));
                orderRequest.setOrderStatus(rs.getString("OrderStatus"));
                orderRequest.setDeliveryAddressId(rs.getObject("DeliveryAddressID") != null ? rs.getInt("DeliveryAddressID") : null);
                
                // Lấy thông tin khách hàng nếu có
                try {
                    orderRequest.setCustomerName(rs.getString("FullName"));
                    orderRequest.setPhone(rs.getString("Phone"));
                } catch (Exception e) {
                    // Không có thông tin khách hàng trong kết quả
                }
                
                list.add(orderRequest);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Helper methods
    private Integer createDeliveryAddress(OrderRequest orderRequest) {
        try {
            // Tạo địa chỉ mới từ thông tin đơn hàng
            Address newAddress = new Address();
            newAddress.setUserId(orderRequest.getUserId());
            newAddress.setAddressLine1(orderRequest.getAddress());
            newAddress.setCity(orderRequest.getCity());
            newAddress.setCountry(orderRequest.getCountry());
            newAddress.setPhone(orderRequest.getPhone());
            newAddress.setCustomerName(orderRequest.getCustomerName());
            newAddress.setIsDefault(false);
            newAddress.setCouponId(null);
            newAddress.setCreatedDate(LocalDateTime.now());
            
            // Lưu địa chỉ mới
            addressDAO.insert(newAddress);
            
            // Lấy AddressID vừa tạo
            List<Address> addresses = addressDAO.selectByUserId(orderRequest.getUserId());
            if (!addresses.isEmpty()) {
                return addresses.get(0).getAddressId();
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo địa chỉ giao hàng: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo địa chỉ giao hàng: " + e.getMessage());
        }
        return null;
    }

    private Integer getLastInsertedOrderId() {
        try {
            // Lấy OrderID vừa tạo bằng cách query lại
            ResultSet rs = XJdbc.executeQuery("SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC");
            if (rs.next()) {
                return rs.getInt("OrderID");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy OrderID vừa tạo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy OrderID vừa tạo: " + e.getMessage());
        }
        return null;
    }
    
    private void ensureUserHasDefaultAddress(Integer userId) {
        try {
            // Sử dụng AddressDAO để kiểm tra và tạo địa chỉ mặc định
            if (!addressDAO.hasAddress(userId)) {
                // Lấy thông tin user để có tên khách hàng
                poly.dao.UserDAO userDAO = new poly.dao.impl.UserDAOImpl();
                poly.entity.User user = userDAO.selectById(userId);
                String customerName = user != null ? user.getFullName() : "Khách hàng mặc định";
                
                // Tạo địa chỉ mặc định
                addressDAO.createDefaultAddress(userId, customerName);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi đảm bảo địa chỉ mặc định: " + e.getMessage());
            // Không throw exception vì đây không phải lỗi nghiêm trọng
        }
    }

    private BigDecimal calculateCartSubtotal(Integer userId) {
        // Tính tổng giá trị giỏ hàng
        BigDecimal subtotal = BigDecimal.ZERO;
        
        try {
            // 1. Tìm giỏ hàng của user
            ShoppingCart cart = cartDAO.findByUserId(userId);
            if (cart == null) {
                return subtotal; // Không có giỏ hàng
            }
            
            // 2. Lấy danh sách sản phẩm trong giỏ hàng
            List<CartItem> cartItems = cartDAO.findCartItemsByCartId(cart.getCartId());
            
            // 3. Tính tổng giá trị
            for (CartItem item : cartItems) {
                Product product = getProductById(item.getProductId());
                if (product != null && product.getUnitPrice() != null && item.getQuantity() != null) {
                    BigDecimal itemTotal = product.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                    subtotal = subtotal.add(itemTotal);
                }
            }
        } catch (Exception e) {
            // Log lỗi nếu cần
            System.err.println("Lỗi khi tính tổng giỏ hàng: " + e.getMessage());
        }
        
        return subtotal;
    }

    private Product getProductById(String productId) {
        // Lấy thông tin sản phẩm từ database
        try {
            ResultSet rs = XJdbc.executeQuery("SELECT * FROM Products WHERE ProductID=?", productId);
            if (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("ProductID"));
                product.setCategoryId(rs.getString("CategoryID"));
                product.setProductName(rs.getString("ProductName"));
                product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setImagePath(rs.getString("ImagePath"));
                
                // Lấy CreatedDate nếu có
                try {
                    if (rs.getTimestamp("CreatedDate") != null) {
                        product.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                    }
                } catch (Exception e) {
                    // Nếu không có CreatedDate thì bỏ qua
                }
                
                // Lấy ImportPrice nếu có
                try {
                    product.setGianhap(rs.getBigDecimal("ImportPrice"));
                } catch (Exception e) {
                    // Nếu không có ImportPrice thì bỏ qua
                }
                
                return product;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void deleteOrderRequestItems(Integer orderId) {
        try {
            XJdbc.executeUpdate("DELETE FROM OrderDetails WHERE OrderID=?", orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 