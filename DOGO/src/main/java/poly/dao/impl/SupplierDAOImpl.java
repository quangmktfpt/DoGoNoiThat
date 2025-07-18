package poly.dao.impl;

import poly.dao.SupplierDAO;
import poly.entity.Supplier;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {
    private final String INSERT_SQL = "INSERT INTO Suppliers (SupplierID, SupplierName, ContactName, Phone, Email, Address) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Suppliers SET SupplierName=?, ContactName=?, Phone=?, Email=?, Address=? WHERE SupplierID=?";
    private final String DELETE_SQL = "DELETE FROM Suppliers WHERE SupplierID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Suppliers";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Suppliers WHERE SupplierID=?";
    private final String SELECT_BY_NAME_SQL = "SELECT * FROM Suppliers WHERE SupplierName LIKE ?";
    private final String SELECT_BY_EMAIL_SQL = "SELECT * FROM Suppliers WHERE Email LIKE ?";
    private final String SELECT_BY_PHONE_SQL = "SELECT * FROM Suppliers WHERE Phone LIKE ?";
    private final String SEARCH_BY_KEYWORD_SQL = "SELECT * FROM Suppliers WHERE SupplierName LIKE ? OR Email LIKE ? OR Phone LIKE ? OR ContactName LIKE ?";

    @Override
    public void insert(Supplier supplier) {
        XJdbc.executeUpdate(INSERT_SQL, 
            supplier.getSupplierId(), 
            supplier.getSupplierName(), 
            supplier.getContactName(), 
            supplier.getPhone(), 
            supplier.getEmail(), 
            supplier.getAddress()
        );
    }

    @Override
    public void update(Supplier supplier) {
        XJdbc.executeUpdate(UPDATE_SQL, 
            supplier.getSupplierName(), 
            supplier.getContactName(), 
            supplier.getPhone(), 
            supplier.getEmail(), 
            supplier.getAddress(), 
            supplier.getSupplierId()
        );
    }

    @Override
    public void delete(String supplierId) {
        XJdbc.executeUpdate(DELETE_SQL, supplierId);
    }

    @Override
    public List<Supplier> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public Supplier selectById(String supplierId) {
        List<Supplier> list = selectBySql(SELECT_BY_ID_SQL, supplierId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Supplier> selectBySupplierName(String supplierName) {
        return selectBySql(SELECT_BY_NAME_SQL, "%" + supplierName + "%");
    }

    @Override
    public List<Supplier> selectByEmail(String email) {
        return selectBySql(SELECT_BY_EMAIL_SQL, "%" + email + "%");
    }

    @Override
    public List<Supplier> selectByPhone(String phone) {
        return selectBySql(SELECT_BY_PHONE_SQL, "%" + phone + "%");
    }

    @Override
    public List<Supplier> searchByKeyword(String keyword) {
        String searchPattern = "%" + keyword + "%";
        return selectBySql(SEARCH_BY_KEYWORD_SQL, searchPattern, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<Supplier> selectBySql(String sql, Object... args) {
        List<Supplier> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getString("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setAddress(rs.getString("Address"));
                list.add(supplier);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}