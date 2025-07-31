package io.github.amsatrio.service;

import java.sql.SQLException;
import java.util.List;

import io.github.amsatrio.dto.PageDto;

public interface CRUDService<T> {
    T findById(Long id);
    List<T> findAll() throws SQLException;
    PageDto<T> findPage(int page, int size) throws SQLException;
    T create(T dto) throws SQLException;
    int update(T dto) throws SQLException;
    int delete(Long id) throws SQLException;
}
