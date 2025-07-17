package poly.dao.impl;

import poly.dao.SupportRequestDAO;
import poly.entity.SupportRequest;
import poly.util.XJdbc;
import java.sql.*;
import java.util.*;

public class SupportRequestDAOImpl implements SupportRequestDAO {
    @Override
    public void insert(SupportRequest request) {
        String sql = "INSERT INTO SupportRequest (subject, content, createdDate, status) VALUES (?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, request.getSubject(), request.getContent(), request.getCreatedDate(), request.getStatus());
    }

    @Override
    public List<SupportRequest> findAll() {
        String sql = "SELECT * FROM SupportRequest ORDER BY createdDate DESC";
        return selectBySql(sql);
    }

    @Override
    public SupportRequest findById(int id) {
        String sql = "SELECT * FROM SupportRequest WHERE id = ?";
        List<SupportRequest> list = selectBySql(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void updateStatus(int id, String status) {
        String sql = "UPDATE SupportRequest SET status = ? WHERE id = ?";
        XJdbc.executeUpdate(sql, status, id);
    }

    private List<SupportRequest> selectBySql(String sql, Object... args) {
        List<SupportRequest> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                SupportRequest req = new SupportRequest();
                req.setId(rs.getInt("id"));
                req.setSubject(rs.getString("subject"));
                req.setContent(rs.getString("content"));
                req.setCreatedDate(rs.getTimestamp("createdDate"));
                req.setStatus(rs.getString("status"));
                list.add(req);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}

/*
-- SQL Chuẩn cho bảng SupportRequest:
CREATE TABLE SupportRequest (
    id INT IDENTITY(1,1) PRIMARY KEY,
    subject NVARCHAR(255) NOT NULL,
    content NVARCHAR(2000) NOT NULL,
    createdDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    status NVARCHAR(50) NOT NULL
);
*/ 