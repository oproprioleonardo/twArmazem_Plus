package me.nullpointer.twarmazemplus.data.dao;

public interface DAO {

    void createTable();
    void saveAll();
    void loadAll();
    void load();
    void save();
    void delete();
    boolean exists();

}
