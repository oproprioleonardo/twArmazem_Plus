package me.nullpointer.armazemplus.data;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection make() {
        try {
            final Configuration configuration = API.getConfiguration();
            String password = configuration.get("MySQL.Password", false);
            String user = configuration.get("MySQL.User", false);
            String host = configuration.get("MySQL.Host", false);
            String port = configuration.get("MySQL.Port", false);
            String database = configuration.get("MySQL.Database", false);
            String type = "jdbc:mysql://";
            String url = type + host + ":" + port + "/" + database;
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.print("§cOcorreu um erro ao se conectar com o seu banco de dados MySQL, verifique a sua config.yml.");
        }
        return null;
    }

}
