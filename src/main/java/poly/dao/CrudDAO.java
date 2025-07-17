package poly.dao;

import java.util.List;

/**
 * Interface CRUD DAO tổng quát cho các entity
 * @param <T> Entity
 * @param <K> Kiểu khóa chính
 */
public interface CrudDAO<T, K> {
    void insert(T entity);
    void update(T entity);
    void delete(K id);
    List<T> selectAll();
    T selectById(K id);
    List<T> selectBySql(String sql, Object... args);
} 