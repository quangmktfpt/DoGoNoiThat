package poly.dao.impl;

import poly.dao.AddressDAO;
import poly.entity.Address;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Implementation của AddressDAO
 * @author quang
 */
public class AddressDAOImpl implements AddressDAO {
    
    private final String INSERT_SQL = "INSERT INTO Addresses (UserID, AddressLine1, City, Country, Phone, CustomerName, IsDefault, CouponID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Addresses SET UserID=?, AddressLine1=?, City=?, Country=?, Phone=?, CustomerName=?, IsDefault=?, CouponID=? WHERE AddressID=?";
    private final String DELETE_SQL = "DELETE FROM Addresses WHERE AddressID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Addresses ORDER BY CreatedDate DESC";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Addresses WHERE AddressID=?";
    private final String SELECT_BY_USER_SQL = "SELECT * FROM Addresses WHERE UserID=? ORDER BY IsDefault DESC, CreatedDate DESC";
    private final String SELECT_DEFAULT_BY_USER_SQL = "SELECT * FROM Addresses WHERE UserID=? AND IsDefault=1";
    private final String SET_DEFAULT_SQL = "UPDATE Addresses SET IsDefault=0 WHERE UserID=?";
    private final String SET_SPECIFIC_DEFAULT_SQL = "UPDATE Addresses SET IsDefault=1 WHERE AddressID=?";
    private final String SEARCH_BY_KEYWORD_SQL = "SELECT * FROM Addresses WHERE AddressLine1 LIKE ? OR City LIKE ? OR Country LIKE ? OR CustomerName LIKE ? ORDER BY CreatedDate DESC";
    private final String SELECT_BY_CITY_SQL = "SELECT * FROM Addresses WHERE City=? ORDER BY CreatedDate DESC";
    private final String SELECT_BY_COUNTRY_SQL = "SELECT * FROM Addresses WHERE Country=? ORDER BY CreatedDate DESC";
    private final String CHECK_HAS_ADDRESS_SQL = "SELECT COUNT(*) as count FROM Addresses WHERE UserID=?";
    private final String CREATE_DEFAULT_ADDRESS_SQL = "INSERT INTO Addresses (UserID, AddressLine1, City, Country, Phone, CustomerName, IsDefault) VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void insert(Address address) {
        XJdbc.executeUpdate(INSERT_SQL, 
            address.getUserId(),
            address.getAddressLine1(),
            address.getCity(),
            address.getCountry(),
            address.getPhone(),
            address.getCustomerName(),
            address.getIsDefault(),
            address.getCouponId()
        );
    }

    @Override
    public void update(Address address) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            address.getUserId(),
            address.getAddressLine1(),
            address.getCity(),
            address.getCountry(),
            address.getPhone(),
            address.getCustomerName(),
            address.getIsDefault(),
            address.getCouponId(),
            address.getAddressId()
        );
    }

    @Override
    public void delete(Integer addressId) {
        XJdbc.executeUpdate(DELETE_SQL, addressId);
    }

    @Override
    public List<Address> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public Address selectById(Integer addressId) {
        List<Address> list = selectBySql(SELECT_BY_ID_SQL, addressId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Address> selectByUserId(Integer userId) {
        return selectBySql(SELECT_BY_USER_SQL, userId);
    }

    @Override
    public Address getDefaultAddress(Integer userId) {
        List<Address> list = selectBySql(SELECT_DEFAULT_BY_USER_SQL, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void setDefaultAddress(Integer addressId, Integer userId) {
        try {
            // Đặt tất cả địa chỉ của user thành không mặc định
            XJdbc.executeUpdate(SET_DEFAULT_SQL, userId);
            
            // Đặt địa chỉ cụ thể thành mặc định
            XJdbc.executeUpdate(SET_SPECIFIC_DEFAULT_SQL, addressId);
            
            System.out.println("✓ Đã đặt địa chỉ " + addressId + " làm mặc định cho user " + userId);
        } catch (Exception e) {
            System.err.println("Lỗi khi đặt địa chỉ mặc định: " + e.getMessage());
            throw new RuntimeException("Không thể đặt địa chỉ mặc định: " + e.getMessage());
        }
    }

    @Override
    public List<Address> searchByKeyword(String keyword) {
        String searchTerm = "%" + keyword + "%";
        return selectBySql(SEARCH_BY_KEYWORD_SQL, searchTerm, searchTerm, searchTerm, searchTerm);
    }

    @Override
    public List<Address> selectByCity(String city) {
        return selectBySql(SELECT_BY_CITY_SQL, city);
    }

    @Override
    public List<Address> selectByCountry(String country) {
        return selectBySql(SELECT_BY_COUNTRY_SQL, country);
    }

    @Override
    public boolean hasAddress(Integer userId) {
        try {
            ResultSet rs = XJdbc.executeQuery(CHECK_HAS_ADDRESS_SQL, userId);
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra địa chỉ: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void createDefaultAddress(Integer userId, String customerName) {
        try {
            XJdbc.executeUpdate(CREATE_DEFAULT_ADDRESS_SQL, 
                userId,
                "Địa chỉ mặc định",
                "Hà Nội",
                "Việt Nam",
                "0900000000",
                customerName != null ? customerName : "Khách hàng mặc định",
                true
            );
            System.out.println("✓ Đã tạo địa chỉ mặc định cho user " + userId);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo địa chỉ mặc định: " + e.getMessage());
            throw new RuntimeException("Không thể tạo địa chỉ mặc định: " + e.getMessage());
        }
    }

    @Override
    public List<Address> selectBySql(String sql, Object... args) {
        List<Address> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Address address = new Address();
                address.setAddressId(rs.getInt("AddressID"));
                address.setUserId(rs.getInt("UserID"));
                address.setAddressLine1(rs.getString("AddressLine1"));
                address.setCity(rs.getString("City"));
                address.setCountry(rs.getString("Country"));
                address.setPhone(rs.getString("Phone"));
                address.setCustomerName(rs.getString("CustomerName"));
                address.setIsDefault(rs.getBoolean("IsDefault"));
                address.setCouponId(rs.getString("CouponID"));
                address.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                list.add(address);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn địa chỉ: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
} 