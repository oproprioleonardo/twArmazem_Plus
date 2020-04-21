package me.nullpointer.twarmazemplus.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    /*
    public static Connection make() {
        try {
            String password = Main.cf.getString("MySQL.Password");
            String user = Main.cf.getString("MySQL.User");
            String host = Main.cf.getString("MySQL.Host");
            String port = Main.cf.getString("MySQL.Port");
            String database = Main.cf.getString("MySQL.Database");
            String type = "jdbc:mysql://";
            String url = type + host + ":" + port + "/" + database;
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.print("§cOcorreu um erro ao se conectar com o seu banco de dados MySQL, verifique a sua config.yml.");
        }
        return null;
    }
     */

}
