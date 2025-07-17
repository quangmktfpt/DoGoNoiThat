package poly.dao.impl;

import poly.dao.CategoryDAO;
import poly.entity.Category;
import poly.util.XJdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private final String INSERT_SQL = "INSERT INTO Categories (CategoryID, CategoryName) VALUES (?, ?)";
    private final String UPDATE_SQL = "UPDATE Categories SET CategoryName=? WHERE CategoryID=?";
    private final String DELETE_SQL = "DELETE FROM Categories WHERE CategoryID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Categories";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Categories WHERE CategoryID=?";

    @Override
    public void insert(Category entity) {
        XJdbc.executeUpdate(INSERT_SQL, entity.getCategoryId(), entity.getCategoryName());
    }

    @Override
    public void update(Category entity) {
        XJdbc.executeUpdate(UPDATE_SQL, entity.getCategoryName(), entity.getCategoryId());
    }

    @Override
    public void delete(String id) {
        XJdbc.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Category> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public Category selectById(String id) {
        List<Category> list = selectBySql(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Category> selectBySql(String sql, Object... args) {
        List<Category> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Category entity = new Category();
                entity.setCategoryId(rs.getString("CategoryID"));
                entity.setCategoryName(rs.getString("CategoryName"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 