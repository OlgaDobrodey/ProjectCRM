package com.itrex.java.lab;

import com.itrex.java.lab.service.ParamConnectionBase;
import org.flywaydb.core.Flyway;

public class Main {

    public static void main(String[] args) {
        System.out.println("Connecting to database...");
        Flyway flyway = Flyway.configure()
                .dataSource(ParamConnectionBase.URL, ParamConnectionBase.USER, ParamConnectionBase.PSW)
                .locations("db/migration")
                .load();
        flyway.migrate();
    }
}
