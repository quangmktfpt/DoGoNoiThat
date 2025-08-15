package poly.dao.impl;

import poly.dao.CouponDAO;
import poly.entity.Coupon;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CouponDAOImpl implements CouponDAO {
    
    private final String INSERT_SQL = "INSERT INTO Coupons (CouponID, Description, DiscountType, DiscountValue, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Coupons SET Description=?, DiscountType=?, DiscountValue=?, StartDate=?, EndDate=?, Status=? WHERE CouponID=?";
    private final String DELETE_SQL = "DELETE FROM Coupons WHERE CouponID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Coupons";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Coupons WHERE CouponID=?";
    private final String SELECT_ACTIVE_SQL = "SELECT * FROM Coupons WHERE StartDate <= ? AND EndDate >= ?";
    private final String SELECT_BY_TYPE_SQL = "SELECT * FROM Coupons WHERE DiscountType=?";
    private final String CHECK_VALID_SQL = "SELECT COUNT(*) FROM Coupons WHERE CouponID=? AND StartDate <= ? AND EndDate >= ?";

    @Override
    public void insert(Coupon coupon) {
        XJdbc.executeUpdate(INSERT_SQL, 
            coupon.getCouponId(), 
            coupon.getDescription(), 
            coupon.getDiscountType(), 
            coupon.getDiscountValue(), 
            coupon.getStartDate(), 
            coupon.getEndDate(),
            coupon.getStatus() != null ? coupon.getStatus() : "Hoạt động"
        );
    }

    @Override
    public void update(Coupon coupon) {
        try {
            System.out.println("🔄 Đang cập nhật mã giảm giá: " + coupon.getCouponId());
            System.out.println("📝 Description: " + coupon.getDescription());
            System.out.println("💰 DiscountType: " + coupon.getDiscountType());
            System.out.println("💸 DiscountValue: " + coupon.getDiscountValue());
            System.out.println("📅 StartDate: " + coupon.getStartDate());
            System.out.println("📅 EndDate: " + coupon.getEndDate());
            System.out.println("📊 Status: " + coupon.getStatus());
            
            XJdbc.executeUpdate(UPDATE_SQL, 
                coupon.getDescription(), 
                coupon.getDiscountType(), 
                coupon.getDiscountValue(), 
                coupon.getStartDate(), 
                coupon.getEndDate(),
                coupon.getStatus() != null ? coupon.getStatus() : "Hoạt động",
                coupon.getCouponId()
            );
            
            System.out.println("✅ Cập nhật thành công mã: " + coupon.getCouponId());
        } catch (Exception e) {
            System.err.println("❌ Lỗi cập nhật mã " + coupon.getCouponId() + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(String couponId) {
        XJdbc.executeUpdate(DELETE_SQL, couponId);
    }

    @Override
    public List<Coupon> selectAll() {
        List<Coupon> list = selectBySql(SELECT_ALL_SQL);
        System.out.println("📊 Đã tải " + list.size() + " mã giảm giá từ database:");
        for (Coupon c : list) {
            System.out.println("  - " + c.getCouponId() + ": " + c.getStatus());
        }
        return list;
    }

    @Override
    public Coupon selectById(String couponId) {
        List<Coupon> list = selectBySql(SELECT_BY_ID_SQL, couponId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Coupon> selectActiveCoupons() {
        LocalDate today = LocalDate.now();
        return selectBySql(SELECT_ACTIVE_SQL, today, today);
    }

    @Override
    public List<Coupon> selectByDiscountType(String discountType) {
        return selectBySql(SELECT_BY_TYPE_SQL, discountType);
    }

    @Override
    public boolean isValidCoupon(String couponId) {
        try {
            LocalDate today = LocalDate.now();
            ResultSet rs = XJdbc.executeQuery(CHECK_VALID_SQL, couponId, today, today);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void markCouponAsUsed(String couponId) {
        // Không làm gì - không dùng IsUsed nữa
    }

    @Override
    public boolean isCouponUsed(String couponId) {
        // Luôn trả về false - không kiểm tra IsUsed
        return false;
    }

    @Override
    public int getDailyUsageCount(String couponId) {
        // Luôn trả về 0 - không giới hạn số lần sử dụng
        return 0;
    }

    @Override
    public boolean canUseCouponToday(String couponId, int maxDailyUsage) {
        // Luôn trả về true - không giới hạn
        return true;
    }

    @Override
    public void incrementDailyUsage(String couponId) {
        // Không làm gì - không theo dõi số lần sử dụng
    }

    @Override
    public List<Coupon> selectBySql(String sql, Object... args) {
        List<Coupon> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setCouponId(rs.getString("CouponID"));
                coupon.setDescription(rs.getString("Description"));
                coupon.setDiscountType(rs.getString("DiscountType"));
                coupon.setDiscountValue(rs.getBigDecimal("DiscountValue"));
                coupon.setStartDate(rs.getDate("StartDate") != null ? rs.getDate("StartDate").toLocalDate() : null);
                coupon.setEndDate(rs.getDate("EndDate") != null ? rs.getDate("EndDate").toLocalDate() : null);
                coupon.setStatus(rs.getString("Status"));
                
                list.add(coupon);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 