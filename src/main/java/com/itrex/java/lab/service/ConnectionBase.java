package com.itrex.java.lab.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBase {

    public Connection getConnection() throws ClassNotFoundException {
        Class.forName(ParamConnectionBase.DRIVER);
        try {
            Connection conn = DriverManager.getConnection(ParamConnectionBase.URL, ParamConnectionBase.USER, ParamConnectionBase.PSW);
            return conn;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
