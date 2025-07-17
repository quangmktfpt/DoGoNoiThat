package poly.dao.impl;

import poly.dao.InventoryDAO;
import poly.entity.InventoryTransaction;
import poly.entity.Product;
import poly.util.XJdbc;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public List<Product> getCurrentInventory() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, CategoryID, ProductName, UnitPrice, Quantity, ImagePath, CreatedDate, ImportPrice FROM Products";
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("ProductID"));
                p.setCategoryId(rs.getString("CategoryID"));
                p.setProductName(rs.getString("ProductName"));
                p.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setImagePath(rs.getString("ImagePath"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                p.setGianhap(rs.getBigDecimal("ImportPrice"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<InventoryTransaction> getInventoryHistoryByProduct(String productId) {
        List<InventoryTransaction> list = new ArrayList<>();
        String sql = "EXEC sp_GetInventoryHistoryByProduct ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, productId)) {
            while (rs.next()) {
                InventoryTransaction it = new InventoryTransaction();
                it.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
                it.setTransactionType(rs.getString("TransactionType"));
                it.setQuantityChange(rs.getInt("QuantityChange"));
                it.setReferenceId(rs.getString("ReferenceID"));
                it.setNotes(rs.getString("Notes"));
                list.add(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<InventoryTransaction> getInventoryHistory(String productName, String categoryId, String type, String fromDate, String toDate) {
        List<InventoryTransaction> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT it.TransactionID, it.ProductID, it.TransactionDate, it.TransactionType, it.QuantityChange, it.ReferenceID, it.Notes, it.UserID, u.Username as Username, p.ProductName FROM InventoryTransactions it JOIN Products p ON it.ProductID = p.ProductID LEFT JOIN Users u ON it.UserID = u.UserID WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (productName != null && !productName.isEmpty()) {
            sql.append(" AND p.ProductName LIKE ?");
            params.add("%" + productName + "%");
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND p.CategoryID = ?");
            params.add(categoryId);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND it.TransactionType = ?");
            params.add(type);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND it.TransactionDate >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND it.TransactionDate <= ?");
            params.add(toDate);
        }
        sql.append(" ORDER BY it.TransactionDate DESC");
        try (ResultSet rs = XJdbc.executeQuery(sql.toString(), params.toArray())) {
            while (rs.next()) {
                InventoryTransaction it = new InventoryTransaction();
                it.setTransactionId(rs.getInt("TransactionID"));
                it.setProductId(rs.getString("ProductID"));
                it.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
                it.setTransactionType(rs.getString("TransactionType"));
                it.setQuantityChange(rs.getInt("QuantityChange"));
                it.setReferenceId(rs.getString("ReferenceID"));
                it.setNotes(rs.getString("Notes"));
                it.setUserId(rs.getObject("UserID") != null ? rs.getInt("UserID") : null);
                // Có thể set thêm username nếu cần
                list.add(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addInventoryTransaction(InventoryTransaction transaction) {
        String sql = "INSERT INTO InventoryTransactions (ProductID, TransactionType, QuantityChange, ReferenceID, Notes, UserID) VALUES (?, ?, ?, ?, ?, ?)";
        int result = XJdbc.executeUpdate(sql,
                transaction.getProductId(),
                transaction.getTransactionType(),
                transaction.getQuantityChange(),
                transaction.getReferenceId(),
                transaction.getNotes(),
                transaction.getUserId()
        );
        return result > 0;
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> list = new ArrayList<>();
        String sql = "EXEC sp_CheckLowStock ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, threshold)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setQuantity(rs.getInt("Quantity"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> searchProducts(String name, String categoryId, Double minPrice, Double maxPrice) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Products WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND ProductName LIKE ?");
            params.add("%" + name + "%");
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND CategoryID = ?");
            params.add(categoryId);
        }
        if (minPrice != null) {
            sql.append(" AND UnitPrice >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND UnitPrice <= ?");
            params.add(maxPrice);
        }
        try (ResultSet rs = XJdbc.executeQuery(sql.toString(), params.toArray())) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("ProductID"));
                p.setCategoryId(rs.getString("CategoryID"));
                p.setProductName(rs.getString("ProductName"));
                p.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setImagePath(rs.getString("ImagePath"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                p.setGianhap(rs.getBigDecimal("ImportPrice"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Product getProductById(String productId) {
        String sql = "SELECT * FROM Products WHERE ProductID = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, productId)) {
            if (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("ProductID"));
                p.setCategoryId(rs.getString("CategoryID"));
                p.setProductName(rs.getString("ProductName"));
                p.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setImagePath(rs.getString("ImagePath"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                p.setGianhap(rs.getBigDecimal("ImportPrice"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 