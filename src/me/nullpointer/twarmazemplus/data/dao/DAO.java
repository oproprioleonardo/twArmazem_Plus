package me.nullpointer.twarmazemplus.data.dao;

import java.sql.SQLException;

public interface DAO<B, T> {

    void createTable();

    void saveAll();

    void loadAll();

    void load(B object) throws SQLException;

    void save(T object);

    void delete(B object);

    boolean exists(B object);

}
