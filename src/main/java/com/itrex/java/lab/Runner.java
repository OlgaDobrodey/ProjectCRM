package com.itrex.java.lab;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {

    public static void main(String[] args) {
        try {
            System.out.println("===================START APP======================");
        } catch (Exception e) {
            log.info("THE END..", e);
        }
        System.out.println("================CLOSE APP===================");
    }
}

