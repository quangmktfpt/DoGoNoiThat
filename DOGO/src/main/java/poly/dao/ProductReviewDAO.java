package poly.dao;

import poly.entity.ProductReview;
import java.util.List;

public interface ProductReviewDAO {
    // Thêm đánh giá mới
    void insert(ProductReview review);
    
    // Lấy tất cả đánh giá của một sản phẩm
    List<ProductReview> getReviewsByProduct(String productId);
    
    // Lấy đánh giá trung bình của sản phẩm
    double getAverageRating(String productId);
    
    // Lấy số lượng đánh giá của sản phẩm
    int getReviewCount(String productId);
    
    // Kiểm tra xem user đã đánh giá sản phẩm chưa
    boolean hasUserReviewed(String productId, Integer userId);
    
    // Kiểm tra xem user đã đánh giá sản phẩm trong đơn hàng cụ thể chưa
    boolean hasUserReviewedInOrder(String productId, Integer userId, Integer orderId);
    
    // Kiểm tra xem user có thể đánh giá sản phẩm không (đã mua và ship complete)
    boolean canUserReviewProduct(String productId, Integer userId);
    
    // Lấy danh sách sản phẩm user đã mua và có thể đánh giá
    List<String> getReviewableProducts(Integer userId);
} 