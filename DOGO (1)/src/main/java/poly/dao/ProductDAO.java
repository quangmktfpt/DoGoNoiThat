package poly.dao;

import poly.entity.Product;
import poly.entity.Category;
import javax.swing.ImageIcon;
import java.util.List;

public interface ProductDAO extends CrudDAO<Product, String> {
    // Lấy danh sách tất cả danh mục
    List<Category> loadAllCategories();

    // Các phương thức tìm kiếm đặc thù
    List<Product> searchByName(String name);
    List<Product> searchByPriceRange(java.math.BigDecimal min, java.math.BigDecimal max);
    List<Product> searchByCategory(String categoryId);
} 