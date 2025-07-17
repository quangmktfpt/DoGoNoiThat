package poly.dao;

import java.util.List;
import poly.entity.InventoryTransaction;
import poly.entity.Product;

public interface InventoryDAO {
    // Lấy danh sách tồn kho hiện tại
    List<Product> getCurrentInventory();

    // Lấy lịch sử xuất nhập kho của 1 sản phẩm
    List<InventoryTransaction> getInventoryHistoryByProduct(String productId);

    // Lấy toàn bộ lịch sử xuất nhập kho (có thể lọc theo nhiều điều kiện)
    List<InventoryTransaction> getInventoryHistory(String productName, String categoryId, String type, String fromDate, String toDate);

    // Thêm giao dịch xuất/nhập kho
    boolean addInventoryTransaction(InventoryTransaction transaction);

    // Kiểm tra sản phẩm tồn kho thấp hơn ngưỡng
    List<Product> getLowStockProducts(int threshold);

    // Tìm kiếm sản phẩm theo tên, danh mục, giá
    List<Product> searchProducts(String name, String categoryId, Double minPrice, Double maxPrice);

    // Lấy chi tiết sản phẩm
    Product getProductById(String productId);
} 