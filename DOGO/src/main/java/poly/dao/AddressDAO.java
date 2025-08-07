package poly.dao;

import poly.entity.Address;
import java.util.List;

/**
 * Interface DAO cho Address
 * @author quang
 */
public interface AddressDAO extends CrudDAO<Address, Integer> {
    
    // Lấy danh sách địa chỉ theo user
    List<Address> selectByUserId(Integer userId);
    
    // Lấy địa chỉ mặc định của user
    Address getDefaultAddress(Integer userId);
    
    // Đặt địa chỉ làm mặc định
    void setDefaultAddress(Integer addressId, Integer userId);
    
    // Tìm kiếm địa chỉ theo từ khóa
    List<Address> searchByKeyword(String keyword);
    
    // Lấy địa chỉ theo thành phố
    List<Address> selectByCity(String city);
    
    // Lấy địa chỉ theo quốc gia
    List<Address> selectByCountry(String country);
    
    // Kiểm tra xem user đã có địa chỉ nào chưa
    boolean hasAddress(Integer userId);
    
    // Tạo địa chỉ mặc định cho user mới
    void createDefaultAddress(Integer userId, String customerName);
} 