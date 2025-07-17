<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.dao;

import poly.entity.Coupon;

/**
 *
 * @author admin
 */
public interface CouponDAO extends CrudDao<Coupon, Integer> {

}
=======
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
} 
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
