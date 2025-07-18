/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.dao;

import poly.entity.Supplier;
import java.util.List;

public interface SupplierDAO extends CrudDAO<Supplier, String> {
    // Lấy danh sách nhà cung cấp theo tên
    List<Supplier> selectBySupplierName(String supplierName);
    
    // Lấy danh sách nhà cung cấp theo email
    List<Supplier> selectByEmail(String email);
    
    // Lấy danh sách nhà cung cấp theo số điện thoại
    List<Supplier> selectByPhone(String phone);
    
    // Tìm kiếm nhà cung cấp theo từ khóa (tên, email, số điện thoại)
    List<Supplier> searchByKeyword(String keyword);
    
    // Lấy nhà cung cấp theo ID
    Supplier selectById(String supplierId);
} 
