package poly.controller;

import poly.entity.InventoryTransaction;
import poly.entity.Product;
import java.util.List;

public interface InventoryController {
    List<Product> getCurrentInventory();
    List<InventoryTransaction> getInventoryHistoryByProduct(String productId);
    List<InventoryTransaction> getInventoryHistory(String productName, String categoryId, String transactionType, String fromDate, String toDate);
    boolean addInventoryTransaction(InventoryTransaction transaction);
    List<Product> getLowStockProducts(int threshold);
    List<Product> searchProducts(String name, String categoryId, Double minPrice, Double maxPrice);
    Product getProductById(String productId);
} 