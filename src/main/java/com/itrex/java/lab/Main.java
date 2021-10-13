package com.itrex.java.lab;

import com.itrex.java.lab.service.ConnectionBase;
import com.itrex.java.lab.service.ParamConnectionBase;
import org.flywaydb.core.Flyway;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        System.out.println("-----------start----------\n");
        System.out.println("Connecting to database...");
        try {
            Connection connection = new ConnectionBase().getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Flyway flyway = Flyway.configure()
                .dataSource(ParamConnectionBase.URL, ParamConnectionBase.USER, ParamConnectionBase.PSW)
                .locations("resources/db/migration")
                .schemas("crm")
                .load();
        flyway.migrate();

        // flyway.clean();

        System.out.println("-----------finish----------\n");
    }
}
