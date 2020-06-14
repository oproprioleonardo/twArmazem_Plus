package me.nullpointer.armazemplus.data.dao;

public interface DAO<B, T> {

    void createTable();

    void saveAll();

    void loadAll();

    void load(B object);

    void save(T object);

    void delete(B object);

    boolean exists(B object);

}
