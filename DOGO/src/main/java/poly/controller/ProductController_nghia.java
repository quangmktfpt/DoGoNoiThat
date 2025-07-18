package poly.controller;

import poly.entity.Product_Nghia;
import poly.entity.Category;
import javax.swing.ImageIcon;
import java.util.List;
import java.math.BigDecimal;

public interface ProductController_nghia extends CrudController<Product_Nghia> {
    // Lấy danh sách tất cả danh mục
    List<Category> loadAllCategories();

    // Upload ảnh sản phẩm, trả về đường dẫn ảnh đã upload
    String uploadImage(String localImagePath);

    // Lấy ảnh sản phẩm từ đường dẫn
    ImageIcon getProductImage(String imagePath);

    // Các phương thức tìm kiếm đặc thù
    List<Product_Nghia> searchByName(String name);
    List<Product_Nghia> searchByPriceRange(BigDecimal min, BigDecimal max);
    List<Product_Nghia> searchByCategory(String categoryId);
} 