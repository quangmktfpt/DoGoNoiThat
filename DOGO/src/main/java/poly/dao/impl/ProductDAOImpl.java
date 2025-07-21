package poly.dao.impl;

import poly.dao.ProductDAO;
import poly.entity.Product;
import poly.util.XJdbc;
import poly.entity.Category;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDAOImpl implements ProductDAO {
    private final String INSERT_SQL = "INSERT INTO Products (ProductID, CategoryID, ProductName, UnitPrice, Quantity, ImagePath, CreatedDate, ImportPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Products SET CategoryID=?, ProductName=?, UnitPrice=?, Quantity=?, ImagePath=?, CreatedDate=?, ImportPrice=? WHERE ProductID=?";
    private final String DELETE_SQL = "DELETE FROM Products WHERE ProductID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Products";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Products WHERE ProductID=?";

    @Override
    public void insert(Product entity) {
        XJdbc.executeUpdate(INSERT_SQL,
            entity.getProductId(),
            entity.getCategoryId(),
            entity.getProductName(),
            entity.getUnitPrice(),
            entity.getQuantity(),
            entity.getImagePath(),
            entity.getCreatedDate(),
            entity.getGianhap()
        );
    }

    @Override
    public void update(Product entity) {
        XJdbc.executeUpdate(UPDATE_SQL,
            entity.getCategoryId(),
            entity.getProductName(),
            entity.getUnitPrice(),
            entity.getQuantity(),
            entity.getImagePath(),
            entity.getCreatedDate(),
            entity.getGianhap(),
            entity.getProductId()
        );
    }

    @Override
    public void delete(String id) {
        XJdbc.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Product> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public Product selectById(String id) {
        List<Product> list = selectBySql(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Product> selectBySql(String sql, Object... args) {
        List<Product> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Product entity = new Product();
                entity.setProductId(rs.getString("ProductID"));
                entity.setCategoryId(rs.getString("CategoryID"));
                entity.setProductName(rs.getString("ProductName"));
                entity.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                entity.setQuantity(rs.getInt("Quantity"));
                entity.setImagePath(rs.getString("ImagePath"));
                entity.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                entity.setGianhap(rs.getBigDecimal("ImportPrice"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Category> loadAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories";
        try {
            ResultSet rs = XJdbc.executeQuery(sql);
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getString("CategoryID"));
                c.setCategoryName(rs.getString("CategoryName"));
                list.add(c);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Tìm kiếm theo tên sản phẩm
    @Override
    public List<Product> searchByName(String name) {
        String sql = "SELECT * FROM Products WHERE ProductName LIKE ?";
        return this.selectBySql(sql, "%" + name + "%");
    }

    // Tìm kiếm theo khoảng giá
    @Override
    public List<Product> searchByPriceRange(java.math.BigDecimal min, java.math.BigDecimal max) {
        String sql = "SELECT * FROM Products WHERE UnitPrice BETWEEN ? AND ?";
        return selectBySql(sql, min, max);
    }

    // Tìm kiếm theo danh mục
    @Override
    public List<Product> searchByCategory(String categoryId) {
        String sql = "SELECT * FROM Products WHERE CategoryID = ?";
        return selectBySql(sql, categoryId);
    }

    public ImageIcon getProductImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return null;
        File imgFile = new File(imagePath);
        if (!imgFile.exists()) return null;
        return new ImageIcon(imgFile.getAbsolutePath());
    }
   
} 