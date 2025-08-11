package poly.dao.impl;

import poly.dao.ProductReviewDAO;
import poly.entity.ProductReview;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductReviewDAOImpl implements ProductReviewDAO {
    
    @Override
    public void insert(ProductReview review) {
        // Sử dụng SQL với OrderID nếu có
        String sql;
        if (review.getOrderId() != null) {
            sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate, OrderID) VALUES (?, ?, ?, ?, ?, ?)";
            XJdbc.executeUpdate(sql, 
                review.getProductId(),
                review.getUserId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate(),
                review.getOrderId()
            );
        } else {
            // Fallback cho trường hợp không có OrderID
            sql = "INSERT INTO ProductReviews (ProductID, UserID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, ?)";
            XJdbc.executeUpdate(sql, 
                review.getProductId(),
                review.getUserId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
            );
        }
    }
    
    @Override
    public List<ProductReview> getReviewsByProduct(String productId) {
        List<ProductReview> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.FullName FROM ProductReviews r " +
                    "JOIN Users u ON r.UserID = u.UserID " +
                    "WHERE r.ProductID = ? " +
                    "ORDER BY r.ReviewDate DESC";
        
        try {
            ResultSet rs = XJdbc.executeQuery(sql, productId);
            while (rs.next()) {
                ProductReview review = new ProductReview();
                review.setReviewId(rs.getInt("ReviewID"));
                review.setProductId(rs.getString("ProductID"));
                review.setUserId(rs.getInt("UserID"));
                review.setRating(rs.getByte("Rating"));
                review.setComment(rs.getString("Comment"));
                review.setReviewDate(rs.getTimestamp("ReviewDate").toLocalDateTime());
                
                // Đọc OrderID nếu có
                try {
                    Integer orderId = rs.getInt("OrderID");
                    if (!rs.wasNull()) {
                        review.setOrderId(orderId);
                    }
                } catch (SQLException e) {
                    // Cột OrderID có thể chưa tồn tại, bỏ qua
                    System.out.println("DEBUG: OrderID column not available in result set");
                }
                
                // Lưu tên người dùng vào một field tạm thời (có thể thêm vào entity sau)
                review.setUserName(rs.getString("FullName"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    @Override
    public double getAverageRating(String productId) {
        String sql = "SELECT AVG(CAST(Rating AS FLOAT)) as avgRating FROM ProductReviews WHERE ProductID = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, productId);
            if (rs.next()) {
                return rs.getDouble("avgRating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    @Override
    public int getReviewCount(String productId) {
        String sql = "SELECT COUNT(*) as count FROM ProductReviews WHERE ProductID = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, productId);
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public boolean hasUserReviewed(String productId, Integer userId) {
        String sql = "SELECT COUNT(*) as count FROM ProductReviews WHERE ProductID = ? AND UserID = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, productId, userId);
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean hasUserReviewedInOrder(String productId, Integer userId, Integer orderId) {
        // Bây giờ bảng đã có cột OrderID, sử dụng trực tiếp
        String sql = "SELECT COUNT(*) as count FROM ProductReviews WHERE ProductID = ? AND UserID = ? AND OrderID = ?";
        try {
            System.out.println("DEBUG: Checking review with OrderID - ProductID: " + productId + ", UserID: " + userId + ", OrderID: " + orderId);
            ResultSet rs = XJdbc.executeQuery(sql, productId, userId, orderId);
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("DEBUG: Found " + count + " reviews for this product/user/order combination");
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("DEBUG: Error checking review with OrderID: " + e.getMessage());
            e.printStackTrace();
            // Fallback về method cũ nếu có lỗi
            System.out.println("DEBUG: Falling back to old method without OrderID");
            return hasUserReviewed(productId, userId);
        }
        return false;
    }
    
    @Override
    public boolean canUserReviewProduct(String productId, Integer userId) {
        // Kiểm tra xem user đã mua sản phẩm và đơn hàng có trạng thái 'Completed'
        String sql = "SELECT COUNT(*) as count FROM Orders o " +
                    "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                    "WHERE od.ProductID = ? AND o.UserID = ? AND o.OrderStatus = 'Completed'";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, productId, userId);
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<String> getReviewableProducts(Integer userId) {
        List<String> products = new ArrayList<>();
        // Lấy danh sách sản phẩm user đã mua và có thể đánh giá
        String sql = "SELECT DISTINCT od.ProductID FROM Orders o " +
                    "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                    "WHERE o.UserID = ? AND o.OrderStatus = 'Completed' " +
                    "AND od.ProductID NOT IN (SELECT ProductID FROM ProductReviews WHERE UserID = ?)";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, userId, userId);
            while (rs.next()) {
                products.add(rs.getString("ProductID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
} 