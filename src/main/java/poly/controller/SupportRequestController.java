package poly.controller;

import poly.dao.SupportRequestDAO;
import poly.dao.impl.SupportRequestDAOImpl;
import poly.entity.SupportRequest;
import java.util.List;

public class SupportRequestController {
    private SupportRequestDAO dao = new SupportRequestDAOImpl();

    public void sendRequest(SupportRequest req) {
        dao.insert(req);
    }

    public List<SupportRequest> getAllRequests() {
        return dao.findAll();
    }

    public void updateStatus(int id, String status) {
        dao.updateStatus(id, status);
    }
} 