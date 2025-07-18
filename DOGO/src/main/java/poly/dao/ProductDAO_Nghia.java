package poly.dao;

import poly.entity.Product;
import poly.entity.Category;
import javax.swing.ImageIcon;
import java.util.List;
import poly.entity.Product_Nghia;

public interface ProductDAO_Nghia extends CrudDAO<Product_Nghia, String> {
    // Lấy danh sách tất cả danh mục
    List<Category> loadAllCategories();

    // Các phương thức tìm kiếm đặc thù
    List<Product_Nghia> searchByName(String name);
    List<Product_Nghia> searchByPriceRange(java.math.BigDecimal min, java.math.BigDecimal max);
    List<Product_Nghia> searchByCategory(String categoryId);
} 