package poly.dao;

import poly.entity.SupportRequest;
import java.util.List;

public interface SupportRequestDAO {
    void insert(SupportRequest request);
    List<SupportRequest> findAll();
    SupportRequest findById(int id);
    void updateStatus(int id, String status);
} 