<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.dao;

import poly.entity.SystemConfig;

/**
 *
 * @author admin
 */
public interface SystemConfigDAO extends CrudDao<SystemConfig, Integer> {

}
=======
package poly.dao;

import poly.entity.SystemConfig;
import java.util.List;

public interface SystemConfigDAO {
    void insert(SystemConfig config);
    void update(SystemConfig config);
    void delete(String configKey);
    SystemConfig findByKey(String configKey);
    List<SystemConfig> findAll();
} 
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
