package poly.dao.impl;

import poly.dao.CouponDAO;
import poly.entity.Coupon;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CouponDAOImpl implements CouponDAO {
    
    private final String INSERT_SQL = "INSERT INTO Coupons (CouponID, Description, DiscountType, DiscountValue, StartDate, EndDate) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Coupons SET Description=?, DiscountType=?, DiscountValue=?, StartDate=?, EndDate=? WHERE CouponID=?";
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
            coupon.getEndDate()
        );
    }

    @Override
    public void update(Coupon coupon) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            coupon.getDescription(), 
            coupon.getDiscountType(), 
            coupon.getDiscountValue(), 
            coupon.getStartDate(), 
            coupon.getEndDate(), 
            coupon.getCouponId()
        );
    }

    @Override
    public void delete(String couponId) {
        XJdbc.executeUpdate(DELETE_SQL, couponId);
    }

    @Override
    public List<Coupon> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
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
                list.add(coupon);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 