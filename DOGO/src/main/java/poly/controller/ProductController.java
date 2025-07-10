package poly.controller;

import poly.entity.Product;
import poly.entity.Category;
import javax.swing.ImageIcon;
import java.util.List;
import java.math.BigDecimal;

public interface ProductController extends CrudController<Product> {
    // Lấy danh sách tất cả danh mục
    List<Category> loadAllCategories();

    // Upload ảnh sản phẩm, trả về đường dẫn ảnh đã upload
    String uploadImage(String localImagePath);

    // Lấy ảnh sản phẩm từ đường dẫn
    ImageIcon getProductImage(String imagePath);

    // Các phương thức tìm kiếm đặc thù
    List<Product> searchByName(String name);
    List<Product> searchByPriceRange(BigDecimal min, BigDecimal max);
    List<Product> searchByCategory(String categoryId);
} 