package com.monapp.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBConnectionPool {
    private static HikariDataSource ds;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // IMPORTANT
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/gestion_etudiants?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(10);

        ds = new HikariDataSource(config);
    }


    public static DataSource getDataSource() {
        return ds;
    }

    private DBConnectionPool() {}
}
