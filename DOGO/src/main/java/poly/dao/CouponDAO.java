package poly.dao;

import poly.entity.Coupon;
import java.util.List;
import java.time.LocalDate;

public interface CouponDAO extends CrudDAO<Coupon, String> {
    
    // Lấy danh sách mã giảm giá còn hiệu lực
    List<Coupon> selectActiveCoupons();
    
    // Lấy danh sách mã giảm giá theo loại
    List<Coupon> selectByDiscountType(String discountType);
    
    // Kiểm tra mã giảm giá có hợp lệ không
    boolean isValidCoupon(String couponId);
    
    // Lấy mã giảm giá theo ID
    Coupon selectById(String couponId);
    
    // Các phương thức này không còn được sử dụng (để tương thích)
    void markCouponAsUsed(String couponId);
    boolean isCouponUsed(String couponId);
    int getDailyUsageCount(String couponId);
    boolean canUseCouponToday(String couponId, int maxDailyUsage);
    void incrementDailyUsage(String couponId);
} 