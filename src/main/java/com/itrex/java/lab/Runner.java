package com.itrex.java.lab;

import com.itrex.java.lab.service.FlywayService;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
    }
}
